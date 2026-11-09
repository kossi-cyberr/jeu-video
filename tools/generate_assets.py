#!/usr/bin/env python3
"""Genere les sprites et les sons originaux de Moto-Taxi Rush.

Les fichiers produits sont places dans assets/ et dedies au domaine public
(CC0 1.0) : voir ASSETS.md. Relancer ce script regenere les memes fichiers.
"""

import math
import os
import random
import wave
from array import array

from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "assets")
SCALE = 4


def box(xy):
    return tuple(value * SCALE for value in xy)


def rounded(draw, xy, radius, fill=None, outline=None, width=1):
    draw.rounded_rectangle(box(xy), radius=radius * SCALE, fill=fill,
                           outline=outline, width=max(1, width * SCALE))


def ellipse(draw, xy, fill=None, outline=None, width=1):
    draw.ellipse(box(xy), fill=fill, outline=outline,
                 width=max(1, width * SCALE))


def polygon(draw, points, fill=None, outline=None):
    draw.polygon([(x * SCALE, y * SCALE) for x, y in points],
                 fill=fill, outline=outline)


def line(draw, points, fill, width=1):
    draw.line([(x * SCALE, y * SCALE) for x, y in points], fill=fill,
              width=max(1, width * SCALE))


def arc(draw, xy, start, end, fill, width=1):
    draw.arc(box(xy), start, end, fill=fill, width=max(1, width * SCALE))


def pieslice(draw, xy, start, end, fill):
    draw.pieslice(box(xy), start, end, fill=fill)


def new_sprite(width, height):
    return Image.new("RGBA", (width * SCALE, height * SCALE), (0, 0, 0, 0))


def save_sprite(image, width, height, name):
    path = os.path.join(ASSETS, "sprites", name)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.resize((width, height), Image.LANCZOS).save(path)


def draw_bike():
    img = new_sprite(60, 100)
    d = ImageDraw.Draw(img)

    # Roues avant et arriere.
    rounded(d, (23, 14, 37, 32), 6, fill=(28, 28, 30), outline=(10, 10, 12))
    rounded(d, (23, 68, 37, 86), 6, fill=(28, 28, 30), outline=(10, 10, 12))
    rounded(d, (26, 18, 34, 28), 3, fill=(70, 70, 75))
    rounded(d, (26, 72, 34, 82), 3, fill=(70, 70, 75))

    # Chassis, reservoir et selle.
    rounded(d, (21, 24, 39, 82), 7, fill=(232, 122, 36), outline=(105, 48, 12))
    ellipse(d, (21, 30, 39, 48), fill=(246, 150, 55), outline=(150, 70, 20))
    rounded(d, (24, 48, 36, 62), 4, fill=(40, 34, 32), outline=(20, 16, 14))

    # Caisse de livraison a l'arriere.
    rounded(d, (19, 54, 41, 73), 4, fill=(240, 196, 48), outline=(135, 95, 15))
    rounded(d, (19, 62, 41, 65), 1, fill=(150, 105, 25))

    # Guidon et poignees.
    rounded(d, (9, 27, 51, 32), 2, fill=(120, 120, 125), outline=(60, 60, 65))
    rounded(d, (7, 26, 15, 33), 3, fill=(25, 25, 28))
    rounded(d, (45, 26, 53, 33), 3, fill=(25, 25, 28))

    # Phare avant.
    ellipse(d, (26, 30, 34, 36), fill=(255, 240, 170), outline=(160, 140, 80))

    # Pilote : casque, visiere et epaules.
    rounded(d, (17, 42, 43, 54), 6, fill=(55, 55, 70), outline=(25, 25, 35))
    ellipse(d, (23, 34, 37, 47), fill=(248, 248, 242), outline=(60, 60, 70))
    rounded(d, (26, 37, 34, 42), 2, fill=(45, 85, 135))

    save_sprite(img, 60, 100, "bike.png")


def draw_car():
    img = new_sprite(70, 110)
    d = ImageDraw.Draw(img)

    # Roues qui depassent legerement de la carrosserie.
    for y in (16, 74):
        rounded(d, (3, y, 12, y + 22), 3, fill=(25, 25, 28))
        rounded(d, (58, y, 67, y + 22), 3, fill=(25, 25, 28))

    # Carrosserie vue du dessus, avant en bas.
    rounded(d, (5, 5, 65, 105), 12, fill=(196, 44, 42), outline=(88, 16, 16))
    rounded(d, (13, 16, 57, 36), 5, fill=(55, 95, 135), outline=(25, 40, 60))
    rounded(d, (13, 37, 57, 62), 4, fill=(158, 30, 28))
    rounded(d, (12, 63, 58, 87), 5, fill=(60, 100, 140), outline=(25, 40, 60))
    rounded(d, (14, 89, 56, 101), 4, fill=(172, 35, 33))

    # Phares, feux arriere et retroviseurs.
    ellipse(d, (11, 96, 24, 105), fill=(255, 240, 160), outline=(140, 120, 60))
    ellipse(d, (46, 96, 59, 105), fill=(255, 240, 160), outline=(140, 120, 60))
    rounded(d, (12, 7, 25, 13), 2, fill=(232, 70, 60))
    rounded(d, (45, 7, 58, 13), 2, fill=(232, 70, 60))
    rounded(d, (1, 66, 8, 73), 2, fill=(80, 20, 20))
    rounded(d, (62, 66, 69, 73), 2, fill=(80, 20, 20))

    save_sprite(img, 70, 110, "car.png")


def draw_truck():
    img = new_sprite(90, 180)
    d = ImageDraw.Draw(img)

    # Roues du camion.
    for y in (16, 58, 130, 160):
        rounded(d, (2, y, 12, y + 22), 3, fill=(25, 25, 28))
        rounded(d, (78, y, 88, y + 22), 3, fill=(25, 25, 28))

    # Remorque puis cabine, avant en bas.
    rounded(d, (9, 5, 81, 112), 8, fill=(70, 105, 160), outline=(30, 50, 90))
    rounded(d, (14, 20, 76, 106), 4, outline=(45, 72, 118))
    rounded(d, (11, 116, 79, 173), 8, fill=(45, 80, 140), outline=(20, 35, 70))
    rounded(d, (17, 149, 73, 167), 4, fill=(60, 100, 140), outline=(25, 40, 60))

    # Phares et calandre.
    ellipse(d, (15, 163, 28, 174), fill=(255, 240, 160), outline=(140, 120, 60))
    ellipse(d, (62, 163, 75, 174), fill=(255, 240, 160), outline=(140, 120, 60))
    rounded(d, (33, 168, 57, 174), 2, fill=(30, 30, 35))

    save_sprite(img, 90, 180, "truck.png")


def draw_pothole():
    img = new_sprite(80, 80)
    d = ImageDraw.Draw(img)

    ellipse(d, (4, 16, 76, 66), fill=(58, 58, 63))
    ellipse(d, (10, 22, 70, 60), fill=(14, 14, 17))
    line(d, [(20, 40), (10, 30), (3, 32)], fill=(32, 32, 36), width=2)
    line(d, [(60, 50), (72, 58), (79, 54)], fill=(32, 32, 36), width=2)
    line(d, [(34, 58), (30, 72), (25, 77)], fill=(32, 32, 36), width=2)
    arc(d, (14, 26, 66, 58), 200, 340, fill=(80, 80, 86), width=1)

    save_sprite(img, 80, 80, "pothole.png")


def draw_passenger():
    img = new_sprite(50, 70)
    d = ImageDraw.Draw(img)

    # Jambes et chaussures.
    rounded(d, (16, 47, 23, 66), 3, fill=(40, 50, 80), outline=(20, 25, 40))
    rounded(d, (27, 47, 34, 66), 3, fill=(40, 50, 80), outline=(20, 25, 40))
    rounded(d, (13, 63, 24, 68), 2, fill=(30, 30, 35))
    rounded(d, (26, 63, 37, 68), 2, fill=(30, 30, 35))

    # Torse et bras qui font signe.
    rounded(d, (12, 26, 38, 52), 7, fill=(240, 196, 48), outline=(135, 95, 15))
    rounded(d, (4, 28, 14, 36), 4, fill=(240, 196, 48), outline=(135, 95, 15))
    rounded(d, (36, 20, 46, 28), 4, fill=(240, 196, 48), outline=(135, 95, 15))
    ellipse(d, (1, 24, 9, 32), fill=(230, 180, 140))
    ellipse(d, (41, 14, 49, 22), fill=(230, 180, 140))

    # Tete, cheveux et visage.
    ellipse(d, (15, 7, 35, 27), fill=(230, 180, 140), outline=(120, 80, 50))
    pieslice(d, (15, 6, 35, 20), 180, 360, fill=(66, 44, 30))
    ellipse(d, (20, 15, 23, 18), fill=(40, 30, 25))
    ellipse(d, (27, 15, 30, 18), fill=(40, 30, 25))
    arc(d, (21, 18, 29, 24), 0, 180, fill=(120, 60, 45), width=1)

    save_sprite(img, 50, 70, "passenger.png")


def draw_heart():
    img = new_sprite(22, 22)
    d = ImageDraw.Draw(img)

    ellipse(d, (1, 3, 11, 13), fill=(210, 45, 45))
    ellipse(d, (11, 3, 21, 13), fill=(210, 45, 45))
    polygon(d, [(2, 8), (20, 8), (11, 20)], fill=(210, 45, 45))
    ellipse(d, (5, 5, 9, 9), fill=(245, 120, 120))

    save_sprite(img, 22, 22, "heart.png")


SAMPLE_RATE = 22050


def save_wav(relative_path, samples):
    path = os.path.join(ASSETS, relative_path)
    os.makedirs(os.path.dirname(path), exist_ok=True)
    peak = max(0.001, max(abs(sample) for sample in samples))
    gain = 0.9 / peak if peak > 0.9 else 1.0
    data = array("h", (
        int(max(-1.0, min(1.0, sample * gain)) * 32767) for sample in samples
    ))
    with wave.open(path, "wb") as wav:
        wav.setnchannels(1)
        wav.setsampwidth(2)
        wav.setframerate(SAMPLE_RATE)
        wav.writeframes(data.tobytes())


def generate_crash():
    random.seed(7)
    count = int(0.45 * SAMPLE_RATE)
    samples = []
    smooth_noise = 0.0
    for i in range(count):
        t = i / SAMPLE_RATE
        envelope = math.exp(-t * 10)
        smooth_noise = smooth_noise * 0.6 + random.uniform(-1, 1) * 0.4
        thump = math.sin(2 * math.pi * (95 - 60 * t / 0.45) * t)
        samples.append((0.55 * smooth_noise + 0.6 * thump) * envelope)
    save_wav("sounds/crash.wav", samples)


def generate_pickup():
    notes = [(880.0, 0.00), (1318.5, 0.09), (1760.0, 0.18)]
    count = int(0.42 * SAMPLE_RATE)
    samples = []
    for i in range(count):
        t = i / SAMPLE_RATE
        value = 0.0
        for frequency, start in notes:
            if t < start:
                continue
            local = t - start
            envelope = math.exp(-local * 11) * min(1.0, local / 0.008)
            value += 0.3 * envelope * math.sin(2 * math.pi * frequency * local)
        samples.append(value)
    save_wav("sounds/pickup.wav", samples)


def add_tone(buffer, start_sample, duration, frequency, amplitude, kind):
    count = int(duration * SAMPLE_RATE)
    for i in range(count):
        index = start_sample + i
        if index >= len(buffer):
            break
        t = i / SAMPLE_RATE
        envelope = min(1.0, t / 0.005) * math.exp(-t * 3.0)
        if kind == "square":
            value = 1.0 if math.sin(2 * math.pi * frequency * t) >= 0 else -1.0
        else:
            value = math.sin(2 * math.pi * frequency * t)
        buffer[index] += amplitude * envelope * value


def add_kick(buffer, start_sample, duration):
    count = int(duration * SAMPLE_RATE)
    for i in range(count):
        index = start_sample + i
        if index >= len(buffer):
            break
        t = i / SAMPLE_RATE
        frequency = 150 - 105 * (t / duration)
        envelope = math.exp(-t * 14)
        buffer[index] += 0.5 * envelope * math.sin(2 * math.pi * frequency * t)


def add_hat(buffer, start_sample, duration):
    random.seed(start_sample)
    count = int(duration * SAMPLE_RATE)
    for i in range(count):
        index = start_sample + i
        if index >= len(buffer):
            break
        t = i / SAMPLE_RATE
        envelope = math.exp(-t * 60)
        buffer[index] += 0.05 * envelope * random.uniform(-1, 1)


def generate_music():
    bpm = 120
    beat = 60 / bpm
    bar = 4 * beat
    bars = 8
    count = int(bars * bar * SAMPLE_RATE)
    samples = [0.0] * count

    chords = [
        [220.00, 261.63, 329.63],  # La mineur
        [174.61, 220.00, 261.63],  # Fa majeur
        [261.63, 329.63, 392.00],  # Do majeur
        [196.00, 246.94, 293.66],  # Sol majeur
    ]
    roots = [110.00, 87.31, 130.81, 98.00]

    for bar_index in range(bars):
        chord = chords[bar_index % 4]
        root = roots[bar_index % 4]
        bar_start = bar_index * bar

        for beat_index in range(4):
            add_tone(samples, int((bar_start + beat_index * beat) * SAMPLE_RATE),
                     beat * 0.9, root, 0.22, "sine")
        for step in range(16):
            octave = 2.0 if step >= 8 else 1.0
            add_tone(samples, int((bar_start + step * beat / 4) * SAMPLE_RATE),
                     beat / 4 * 0.9, chord[step % 3] * octave, 0.075, "square")
        for beat_index in (0, 2):
            add_kick(samples, int((bar_start + beat_index * beat) * SAMPLE_RATE), 0.18)
        for step in range(8):
            add_hat(samples,
                    int((bar_start + (step + 0.5) * beat / 2) * SAMPLE_RATE), 0.05)

    save_wav("music/theme.wav", samples)


def main():
    os.makedirs(os.path.join(ASSETS, "sprites"), exist_ok=True)
    os.makedirs(os.path.join(ASSETS, "sounds"), exist_ok=True)
    os.makedirs(os.path.join(ASSETS, "music"), exist_ok=True)

    draw_bike()
    draw_car()
    draw_truck()
    draw_pothole()
    draw_passenger()
    draw_heart()

    generate_crash()
    generate_pickup()
    generate_music()

    print("Assets generes dans", ASSETS)


if __name__ == "__main__":
    main()
