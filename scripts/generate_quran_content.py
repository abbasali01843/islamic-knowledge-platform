#!/usr/bin/env python3
"""Generate the offline Quran reader asset used by the Android app.

Arabic text comes from quran-json (Uthmani text sourced from The Noble Qur'an
Encyclopedia). Bengali meanings come verbatim from QuranEnc's Bengali Rowwad
translation API. Structural Hafs metadata comes from Al Quran Cloud's Quran
API. The generated asset is bundled into the APK, so the app does not need
network access to read the Quran.
"""

from __future__ import annotations

import argparse
import json
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path
from urllib.request import Request, urlopen

ARABIC_URL = "https://cdn.jsdelivr.net/npm/quran-json@3.1.2/dist/quran.json"
TRANSLATION_URL = "https://quranenc.com/api/v1/translation/sura/bengali_rwwad/{sura}"
METADATA_URL = "https://api.alquran.cloud/v1/juz/{juz}/quran-uthmani"
BENGALI_VERSION = "1.1.2"
METADATA_SOURCE_VERSION = "Al Quran Cloud API / Hafs metadata"


def get_json(url: str):
    request = Request(url, headers={"User-Agent": "IslamicKnowledgePlatform/0.2"})
    with urlopen(request, timeout=45) as response:
        return json.load(response)


def _extract_rows(payload):
    """Normalize QuranEnc responses across API response shapes."""
    if isinstance(payload, list):
        return payload
    if not isinstance(payload, dict):
        return None

    for key in ("result", "data", "translations"):
        value = payload.get(key)
        if isinstance(value, list):
            return value
        if isinstance(value, dict):
            nested = _extract_rows(value)
            if nested is not None:
                return nested

    # Some API variants return an object keyed by ayah number.
    if payload and all(str(key).isdigit() for key in payload):
        rows = []
        for key, value in payload.items():
            if isinstance(value, dict):
                row = dict(value)
                row.setdefault("aya", int(key))
                rows.append(row)
            elif isinstance(value, str):
                rows.append({"aya": int(key), "translation": value})
        return rows

    return None


def fetch_translation(sura: int):
    payload = get_json(TRANSLATION_URL.format(sura=sura))
    rows = _extract_rows(payload)
    if not isinstance(rows, list) or not rows:
        raise RuntimeError(f"Unexpected QuranEnc response for surah {sura}")
    return sura, rows


def fetch_juz_metadata(juz: int):
    payload = get_json(METADATA_URL.format(juz=juz))
    data = payload.get("data") if isinstance(payload, dict) else None
    ayahs = data.get("ayahs") if isinstance(data, dict) else None
    if not isinstance(ayahs, list) or not ayahs:
        raise RuntimeError(f"Unexpected Al Quran Cloud metadata for juz {juz}")
    return juz, ayahs


def normalize_sajda(value) -> bool:
    if isinstance(value, bool):
        return value
    return isinstance(value, dict) and bool(value)


def load_structural_metadata():
    """Load Hafs structural metadata keyed by absolute ayah number."""
    metadata_by_ayah = {}
    with ThreadPoolExecutor(max_workers=8) as pool:
        futures = {pool.submit(fetch_juz_metadata, juz): juz for juz in range(1, 31)}
        for future in as_completed(futures):
            juz, ayahs = future.result()
            for item in ayahs:
                if not isinstance(item, dict):
                    continue
                absolute_number = item.get("number")
                if absolute_number is None:
                    continue
                absolute_number = int(absolute_number)
                hizb_quarter = item.get("hizbQuarter")
                metadata_by_ayah[absolute_number] = {
                    "juz": int(item["juz"]) if item.get("juz") is not None else juz,
                    "hizb": ((int(hizb_quarter) - 1) // 4 + 1) if hizb_quarter is not None else None,
                    "page": int(item["page"]) if item.get("page") is not None else None,
                    "hasSajdah": normalize_sajda(item.get("sajda")),
                }

    if len(metadata_by_ayah) != 6236:
        raise RuntimeError(f"Expected metadata for 6236 ayahs, got {len(metadata_by_ayah)}")
    return metadata_by_ayah


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", required=True)
    args = parser.parse_args()

    arabic = get_json(ARABIC_URL)
    if not isinstance(arabic, list) or len(arabic) != 114:
        raise RuntimeError("Arabic Quran source did not contain 114 surahs")

    translations_by_sura = {}
    with ThreadPoolExecutor(max_workers=8) as pool:
        futures = {pool.submit(fetch_translation, sura): sura for sura in range(1, 115)}
        for future in as_completed(futures):
            sura, rows = future.result()
            translations_by_sura[sura] = rows

    structural_metadata = load_structural_metadata()

    surahs = []
    total_ayahs = 0
    absolute_ayah_number = 0
    for chapter in arabic:
        number = int(chapter["id"])
        verses = chapter.get("verses", [])
        translations_for_sura = {}
        for item in translations_by_sura[number]:
            if not isinstance(item, dict) or "aya" not in item:
                continue
            translations_for_sura[int(item["aya"])] = item.get("translation", "")

        ayahs = []
        for verse in verses:
            ayah_number = int(verse["id"])
            absolute_ayah_number += 1
            bengali = translations_for_sura.get(ayah_number)
            if bengali is None:
                raise RuntimeError(f"Missing Bengali translation {number}:{ayah_number}")
            metadata = structural_metadata.get(absolute_ayah_number)
            if metadata is None:
                raise RuntimeError(f"Missing structural metadata for {number}:{ayah_number}")
            ayahs.append({
                "number": ayah_number,
                "arabic": verse["text"],
                "bengali": bengali,
                **metadata,
            })
        total_ayahs += len(ayahs)
        surahs.append({
            "number": number,
            "ayahCount": len(ayahs),
            "ayahs": ayahs,
        })

    if total_ayahs != 6236 or absolute_ayah_number != 6236:
        raise RuntimeError(f"Expected 6236 ayahs, got {total_ayahs}")

    document = {
        "schemaVersion": 2,
        "generatedAt": "2026-09-10",
        "arabicSource": {
            "name": "Quran JSON",
            "version": "3.1.2",
            "url": ARABIC_URL,
            "attribution": "Uthmani Quran text sourced from The Noble Qur'an Encyclopedia.",
        },
        "bengaliSource": {
            "name": "QuranEnc Bengali Rowwad",
            "version": BENGALI_VERSION,
            "url": "https://quranenc.com/bn/browse/bengali_rwwad",
            "publisher": "Rowwad Translation Center in cooperation with IslamHouse.com",
            "attribution": "Bengali translation of the meanings from QuranEnc.com.",
        },
        "structuralMetadataSource": {
            "name": "Al Quran Cloud API",
            "version": METADATA_SOURCE_VERSION,
            "url": "https://api.alquran.cloud/v1",
            "attribution": "Hafs structural metadata: Juz, Hizb, page and Sajdah markers.",
        },
        "surahs": surahs,
    }

    output = Path(args.output)
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(document, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"Generated {output} with {len(surahs)} surahs and {total_ayahs} ayahs")


if __name__ == "__main__":
    main()
