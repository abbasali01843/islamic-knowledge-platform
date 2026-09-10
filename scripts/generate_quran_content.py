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
TRANSLATION_LIST_URL = "https://quranenc.com/api/v1/translations/list/bn?localization=bn"
TRANSLATION_URL = "https://quranenc.com/api/v1/translation/sura/bengali_rwwad/{sura}"


def get_json(url: str):
    request = Request(url, headers={"User-Agent": "IslamicKnowledgePlatform/0.2"})
    with urlopen(request, timeout=45) as response:
        return json.load(response)


def fetch_translation(sura: int):
    payload = get_json(TRANSLATION_URL.format(sura=sura))
    rows = payload.get("result", payload)
    if not isinstance(rows, list):
        raise RuntimeError(f"Unexpected QuranEnc response for surah {sura}")
    return sura, rows


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--output", required=True)
    args = parser.parse_args()

    arabic = get_json(ARABIC_URL)
    if not isinstance(arabic, list) or len(arabic) != 114:
        raise RuntimeError("Arabic Quran source did not contain 114 surahs")

    translation_list = get_json(TRANSLATION_LIST_URL)
    translations = translation_list.get("result", translation_list)
    rowwad = next((item for item in translations if item.get("key") == "bengali_rwwad"), None)
    if not rowwad:
        raise RuntimeError("bengali_rwwad translation was not found in QuranEnc")

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
        translations_for_sura = {
            int(item["aya"]): item.get("translation", "")
            for item in translations_by_sura[number]
        }
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
            "version": rowwad.get("version", "1.1.2"),
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
