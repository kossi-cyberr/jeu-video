#!/usr/bin/env python3
"""Compile la documentation du jeu (docs/src/*.html) en PDF (docs/pdf/).

Utilise Chrome en mode sans interface : le rendu HTML/CSS est fidele et les
images des captures sont integrees telles quelles.

Usage : python3 tools/build_docs.py
"""

import os
import shutil
import subprocess
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(ROOT, "docs", "src")
OUT = os.path.join(ROOT, "docs", "pdf")

DOCUMENTS = [
    ("01-presentation-et-regles.html", "01-presentation-et-regles.pdf"),
    ("02-architecture-technique.html", "02-architecture-technique.pdf"),
    ("03-installation-et-build.html", "03-installation-et-build.pdf"),
    ("04-assets-tests-qualite.html", "04-assets-tests-qualite.pdf"),
]

CHROME_CANDIDATES = ["google-chrome", "chromium", "chromium-browser", "chrome"]


def find_chrome():
    for name in CHROME_CANDIDATES:
        path = shutil.which(name)
        if path:
            return path
    sys.exit("Chrome introuvable : installer Chrome ou Chromium.")


def build(chrome, source, target):
    command = [
        chrome,
        "--headless=new",
        "--disable-gpu",
        "--no-sandbox",
        "--no-pdf-header-footer",
        "--allow-file-access-from-files",
        "--virtual-time-budget=4000",
        "--print-to-pdf=" + target,
        "file://" + source,
    ]
    result = subprocess.run(command, capture_output=True, text=True)
    if result.returncode != 0 or not os.path.exists(target):
        sys.stderr.write(result.stdout + result.stderr)
        sys.exit("Echec de generation : %s" % source)


def main():
    chrome = find_chrome()
    os.makedirs(OUT, exist_ok=True)
    for source_name, target_name in DOCUMENTS:
        source = os.path.join(SRC, source_name)
        target = os.path.join(OUT, target_name)
        if not os.path.exists(source):
            sys.exit("Fichier manquant : %s" % source)
        build(chrome, source, target)
        print("%s -> %s (%d octets)"
              % (source_name, target_name, os.path.getsize(target)))


if __name__ == "__main__":
    main()
