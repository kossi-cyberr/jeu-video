#!/usr/bin/env python3
"""Genere le medaillon photo du menu principal.

Source : tools/portrait_source.jpg (photo fournie par l'auteur du jeu).
Sortie : assets/sprites/portrait.png, un disque de 320 px avec un anneau
orange, le visage net sur un fond floute et assombri.

Le buste est detoure par un polygone trace a la main : un detourage
automatique accroche le mur clair et les lettres de l'arriere-plan.

Usage : python3 tools/make_portrait.py
"""

import os

from PIL import Image, ImageDraw, ImageFilter

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SOURCE = os.path.join(ROOT, "tools", "portrait_source.jpg")
TARGET = os.path.join(ROOT, "assets", "sprites", "portrait.png")

OUT = 320                  # taille finale (affichee a 110 px, marge pour le retina)
N = OUT * 4                # plan de travail suréchantillonne
CROP = (112, 200, 492, 580)  # carre centre sur le visage, dans la photo 607x1080

# Contour du buste, en coordonnees locales du cadrage (0..380).
BUST = [
    (-40, 400), (-40, 380), (20, 380), (45, 344), (78, 322), (112, 310),
    (140, 302), (152, 288), (135, 276), (114, 264), (100, 248), (88, 232),
    (86, 208), (78, 188), (74, 162), (74, 132), (80, 102), (92, 78),
    (110, 60), (134, 48), (160, 44), (186, 46), (208, 56), (228, 74),
    (244, 96), (252, 122), (256, 150), (258, 178), (258, 200), (252, 222),
    (244, 246), (238, 264), (228, 280), (230, 296), (256, 306), (292, 320),
    (322, 334), (356, 352), (420, 380), (420, 400),
]


def circle_mask(ratio):
    m = Image.new("L", (N, N), 0)
    c, r = N / 2, N * ratio
    ImageDraw.Draw(m).ellipse((c - r, c - r, c + r, c + r), fill=255)
    return m


def ring(ratio, color, width):
    layer = Image.new("RGBA", (N, N), (0, 0, 0, 0))
    c, r = N / 2, N * ratio
    ImageDraw.Draw(layer).ellipse((c - r, c - r, c + r, c + r),
                                  outline=color, width=max(1, int(N * width)))
    return layer


def main():
    square = Image.open(SOURCE).convert("RGB").crop(CROP).resize(
        (N, N), Image.LANCZOS)
    scale = N / 380.0

    mask = Image.new("L", (N, N), 0)
    ImageDraw.Draw(mask).polygon(
        [(x * scale, y * scale) for x, y in BUST], fill=255)
    # Erosion : le bord du masque doit tomber dans les cheveux sombres, sinon
    # la paroi claire de la photo transparait en liseré autour de la tête.
    mask = mask.resize((N // 4, N // 4), Image.LANCZOS)
    mask = mask.filter(ImageFilter.MinFilter(19)).filter(ImageFilter.MaxFilter(5))
    mask = mask.resize((N, N), Image.LANCZOS).filter(ImageFilter.GaussianBlur(3))

    # Fond du cercle : la meme photo, floutee et assombrie, teintee vers le bas.
    bg = square.filter(ImageFilter.GaussianBlur(N / 26))
    bg = Image.blend(bg, Image.new("RGB", (N, N), (16, 17, 21)), 0.87)
    warm = Image.new("RGB", (N, N), (242, 130, 40))
    grad = Image.new("L", (1, N))
    for y in range(N):
        grad.putpixel((0, y), int(70 * (y / N) ** 2))
    bg = Image.composite(warm, bg, grad.resize((N, N)).point(lambda v: 0))
    bg = Image.blend(bg, Image.new("RGB", (N, N), (60, 42, 32)), 0.15)
    bg = bg.convert("RGBA")

    cut = square.convert("RGBA")
    cut.putalpha(mask)
    art = Image.alpha_composite(bg, cut)

    out = Image.new("RGBA", (N, N), (0, 0, 0, 0))
    out.paste(art, (0, 0), circle_mask(0.47).filter(ImageFilter.GaussianBlur(N / 400)))
    out = Image.alpha_composite(out, ring(0.463, (242, 115, 26, 90), 0.004))
    out = Image.alpha_composite(out, ring(0.475, (242, 115, 26, 255), 0.008))

    os.makedirs(os.path.dirname(TARGET), exist_ok=True)
    out.resize((OUT, OUT), Image.LANCZOS).save(TARGET)
    print("wrote %s" % TARGET)


if __name__ == "__main__":
    main()
