#!/usr/bin/env python3
"""Generate the offline Quran reader asset used by the Android app.

Arabic text comes from quran-json (Uthmani text sourced from The Noble Qur'an
Encyclopedia). Bengali meanings come verbatim from QuranEnc's Bengali Rowwad
translation API. The generated asset is bundled into the APK, so the app does
not need network access to read the Quran.
"""

from __future__ import annotations

import argparse
import json
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path
from urllib.request import Request, urlopen

ARABIC_URL = "https://cdn.jsdelivr.net/npm/quran-json@3.1.2/dist/quran.json"
TRANSLATION_URL = "https://quranenc.com/api/v1/translation/sura/bengali_rwwad/{sura}"
BENGALI_VERSION = "1.1.2"


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

    surahs = []
    total_ayahs = 0
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
            bengali = translations_for_sura.get(ayah_number)
            if bengali is None:
                raise RuntimeError(f"Missing Bengali translation {number}:{ayah_number}")
            ayahs.append({
                "number": ayah_number,
                "arabic": verse["text"],
                "bengali": bengali,
            })
        total_ayahs += len(ayahs)
        surahs.append({
            "number": number,
            "ayahCount": len(ayahs),
            "ayahs": ayahs,
        })

    if total_ayahs != 6236:
        raise RuntimeError(f"Expected 6236 ayahs, got {total_ayahs}")

    document = {
        "schemaVersion": 1,
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
        "surahs": surahs,
    }

    output = Path(args.output)
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text(json.dumps(document, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"Generated {output} with {len(surahs)} surahs and {total_ayahs} ayahs")


if __name__ == "__main__":
    main()
