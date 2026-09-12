#!/usr/bin/env python3
"""Validate the generated Quran reader asset before Android builds."""

import argparse
import json
from pathlib import Path


EXPECTED_SURAHS = 114
EXPECTED_AYAHS = 6236


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("asset", type=Path)
    args = parser.parse_args()

    document = json.loads(args.asset.read_text(encoding="utf-8"))
    surahs = document.get("surahs")
    if not isinstance(surahs, list) or len(surahs) != EXPECTED_SURAHS:
        raise SystemExit(f"Expected {EXPECTED_SURAHS} surahs, got {len(surahs) if isinstance(surahs, list) else 'invalid'}")

    numbers = [surah.get("number") for surah in surahs]
    if numbers != list(range(1, EXPECTED_SURAHS + 1)):
        raise SystemExit("Surah numbers are missing, duplicated, or out of order")

    ayah_count = 0
    for surah in surahs:
        ayahs = surah.get("ayahs")
        if not isinstance(ayahs, list) or not ayahs:
            raise SystemExit(f"Surah {surah.get('number')} has no ayahs")
        for ayah in ayahs:
            if not isinstance(ayah.get("number"), int):
                raise SystemExit(f"Surah {surah.get('number')} contains an invalid ayah number")
            if not ayah.get("arabic") or not ayah.get("bengali"):
                raise SystemExit(f"Surah {surah.get('number')}, ayah {ayah.get('number')} has empty text")
            ayah_count += 1

    if ayah_count != EXPECTED_AYAHS:
        raise SystemExit(f"Expected {EXPECTED_AYAHS} ayahs, got {ayah_count}")

    source = document.get("bengaliSource")
    if not isinstance(source, dict) or not source.get("name") or not source.get("version"):
        raise SystemExit("Missing Bengali source attribution metadata")

    print(f"Quran asset valid: {len(surahs)} surahs, {ayah_count} ayahs")
    print(f"Bengali source: {source['name']} / {source['version']}")


if __name__ == "__main__":
    main()
