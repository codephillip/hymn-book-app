# Hymn App v2.0.0 — Home Songs Corrections: Change Report

Branch: `v3` · Data file: `app/src/main/res/raw/hymns.json` · All corrections apply to **Home Songs (HS)**.

**Result:** Home Songs now run **1–73 with no duplicate numbers and no gaps** (was 3 duplicate numbers and 1 gap). Entry count 219 → 217.

---

## 1. Summary table

| # | Item | Status | What changed |
|---|---|---|---|
| 1 | Number alongside title | **Done** | Song screen header now reads `7. OKUKIRIZA MWAANA` |
| 2 | Title colour / weight / size | **Awaiting decision** | Two options open — see [Open items](#3-open-items) |
| 3 | Table of contents | **Not started** | |
| 4 | Songs by category + font size | **Not started** | Font size control already exists in the song screen |
| 5 | Hymn 2 — repeated title | **Not started** | |
| 6 | Hymn 7 — duplicate | **Done** | Deleted `GGWE OLWAZI OLW'AMAANYI` |
| 7 | Hymn 17 — 5 verses only | **Done** | Deleted verses 5 and 7, renumbered |
| 8 | Hymn 24 — duplicate | **Done** | Deleted duplicate `I AM THE BREAD OF LIFE` |
| 9 | Hymn 30 — 4 verses only | **Done** | Deleted verse 5 |
| 10 | Hymn 25 — duplicate | **Done** | `MUJJE TUFUNE EKISONYIWO` renumbered 25 → 27 |
| — | Hymn 32 — 5 verses only | **Done** | Restructured 7 verses → 5 |
| — | Hymn 39 — duplicate | **Done** | `TUKWEBAZA KABAKA ABUKESEZA` renumbered 39 → 42 |
| — | Hymn 41 — two line fixes | **Done** | `Nkwebaze ntya`, `Nkwesaza obulamu` |
| — | Hymn 44 — new version | **Blocked** | No attachment received |
| — | Hymn 48 — duplicate | **Done** | `HOSSANA HOSSANA YEZU KABAKA` renumbered 48 → 52 |
| — | Hymn 53 — retitle | **Done** | Title → `YAZUUKIZA LAZARO` |
| — | Hymn 55 — verse 3 lines | **Done** | Lines 3 and 4 replaced |
| — | Hymn 62 — duplicate + chorus | **Already correct** | Duplicate removed in an earlier commit; only fixed a spelling |
| — | Hymn 64 — verses 3 and 4 | **Done** | Verse 3 para 2 replaced; verse 4 replaced, rest deleted |
| — | Hymn 65 — 5 verses only | **Done** | Deleted verse 5, renumbered 6 → 5 |
| — | Hymn 66 — 3 verses only | **Done** | Deleted verse 4 |

---

## 2. Detail of each change

### Item 1 — Song number alongside the title

**Files:** `app/src/main/java/com/codephillip/app/hymnbook/SongFragment.java:257`, `app/src/main/res/layout/fragment_song.xml:37`

```java
// before
titleView.setText(cursor.getTitle());
// after
titleView.setText(String.format(Locale.US, "%d. %s", cursor.getNumber(), cursor.getTitle()));
```

The song reading screen header now shows `7. OKUKIRIZA MWAANA` instead of `OKUKIRIZA MWAANA`. The line beneath it still reads `Hymn 7 • 5 verses`. The layout's design-preview text was updated to match. `./gradlew :app:compileDebugJavaWithJavac` passes.

### Item 6 — Hymn 7 duplicate

Deleted entry `id: 152` — `GGWE OLWAZI OLW'AMAANYI`, Hymn 7, `EZOKUSINZA - HS`. Confirmed the complaint: Luganda title, English body.

Nothing was lost. Its lyrics are the same song as Hymn 2 `FOREVER I CONFESS I BELIEVE` (`EZAMAYINGIRA - HS`), which remains in the book. Hymn 7 is now uniquely **`OKUKIRIZA MWAANA`**.

### Item 7 — Hymn 17 `OBUYINZA BWO NGA BUNGI!`

Had 7 verses; the note asked for verses 5 and 7 to go, leaving 5.

| Was | Now |
|---|---|
| 1. Obuyinza bwo nga bungi! | 1. unchanged |
| 2. Omasamasa otukula, | 2. unchanged |
| 3. Teri kikusemberera, | 3. unchanged |
| 4. Mu buyinza totembeka. | 4. unchanged |
| 5. Ggw'ensibuko y'okwagala. | **deleted** |
| 6. Byonna byetulina mu nsi, | **renumbered to 5** |
| 7. Tumwagale, tumusinze, | **deleted** |

The surviving verse 6 was renumbered to 5 so the sequence runs 1–5 without a gap. The note did not ask for this; say if you would rather keep the original numbers.

### Item 8 — Hymn 24 duplicate

Deleted entry `id: 170` — `I AM THE BREAD OF LIFE`, Hymn 24, `EZA OMUGAATI OGW'OBULAMU - HS`. Hymn 24 is now uniquely **`YIMBA YIMBA MWOYO GWANGE`**.

**The note said "→ Hymn 73", which could not be applied literally.** The song already existed at **Hymn 72** (`SPECIAL SONGS - HS`) in a better-formatted version, and Hymn 73 is `KIRIMUGASAKI`. Deleting the copy at 24 loses no content and leaves the `SPECIAL SONGS` block (71/72/73) untouched.

**To confirm with the author:** is **72** the right number for this song? If the printed book says 73, the whole `SPECIAL SONGS` block needs renumbering, not just this song.

### Item 9 — Hymn 30 `NKUSINZE NTYA NZE NNO NKWAGALE NTYA?`

Verse 5 (`Ndi mutuuze mu nsi ne mu ggulu…`) deleted. Song now runs 1–4. No renumbering needed — it was the last verse.

### Item 10 — Hymn 25 duplicate

`MUJJE TUFUNE EKISONYIWO` renumbered **25 → 27**. Hymn 25 is now uniquely **`BULI YENNA AGULYAKO GUMUBUDABUDA`**.

```
24  YIMBA YIMBA MWOYO GWANGE          28  MUJJE TUMULYE YEZU KABAKA
25  BULI YENNA AGULYAKO GUMUBUDABUDA  29  MWESUNGA KI
26  DINDU DINDU DINDU                 30  NKUSINZE NTYA NZE NNO NKWAGALE NTYA?
27  MUJJE TUFUNE EKISONYIWO           31  NKWEBAZA YEZU KABAKA
```

On *"move it to its appropriate place/position"* — no separate move was needed. Every query sorts by number (`orderByNumber`), and inside the JSON the entries are ordered alphabetically by title within each category, which already places this song between 26 and 28.

### Hymn 32 `OMUGAATI OGW'OBULAMU TUGULYENGA BULIJJO`

Restructured from 7 verses to 5. Chorus unchanged.

| Was | Now |
|---|---|
| 1. Omugaati Ogw'Obulamu, | 1. unchanged |
| 2. Oluba okugulyako, (lines 1–2) | **merged into new verse 2** |
| 3. Akuwonya n'endwadde zo, (lines 3–4) | **merged into new verse 2** |
| 4. Kino nze nakilojja ntya? | **deleted** |
| 5. Alizukiza n'ababe | **renumbered to 3** |
| 6. Wulira gano amakula, | **renumbered to 4** |
| 7. Kabaka nkwaniriza nnyo, | **renumbered to 5** |

New verse 2:

```
2. Oluba okugulyako,
Osonyiyibwa ebibi!
Mu mubiri akuwa obulamu,
Nobaawo nga tolwala!
```

### Hymn 39 duplicate

`TUKWEBAZA KABAKA ABUKESEZA` (`EZO KWEBAZA - HS`) renumbered **39 → 42**. Hymn 39 is now uniquely **`TUMWAGALE YEZU KABAKA`**. Number 42 was free.

### Hymn 41 `KATONDA WANGE OMWAGALWA`

| Location | Was | Now |
|---|---|---|
| Verse 1, line 2 | `Nkusinze ntya, nkwagale ntya?!` | `Nkwebaze ntya, nkwagale ntya?!` |
| Verse 2, line 1 | `Nkwebaze obulamu bwompa,` | `Nkwesaza obulamu bwompa,` |

### Hymn 44 — **BLOCKED**

The note says a new version of the song was attached. **No attachment was received.** `TWEYANZIZA NNYO TWEYANZE GGE` is untouched and still the old six-verse version. Send the new text and it can be swapped in.

### Hymn 48 duplicate

`HOSSANA HOSSANA YEZU KABAKA` (`EZA MATABI - HS`) renumbered **48 → 52**. Hymn 48 is now uniquely **`SSANYU LINGI NNYO`**. Number 52 was free.

### Hymn 53 retitle

Title changed from `YEZU KABAKA AYINGIRA YERUZALEMU MU KITIIBWA` to **`YAZUUKIZA LAZARO`**. This fits — verse 1 already opens `Yazuukiza Lazaro!!`. Category left as `EZA MATABI - HS`.

### Hymn 55 `MWAANA YAKIRABIRAWO`

Verse 3:

| Line | Was | Now |
|---|---|---|
| 3 | `Yenna akiriza ebigambo bye` | `Ebigambo bye bibune ensi;` |
| 4 | `Awo-ne oku-zikirira.` | `Tuwo-ne oku-zikirira.` |

### Hymn 62 `YEZU KABAKA YA TUNUNULA` — already correct

Both parts of the note were already satisfied:

- **"Was repeated twice. Delete one."** — the duplicate (`id: 201`) was already deleted in commit `99e96fb`. The songs now at 62 and 63 (`YEZU KABAKA YA TUNUNULA` / `YEZU KABAKA YAZUKIRA`) were verified to be genuinely different songs.
- **"After Verse 4, the chorus … is x2"** — already present.

One change made: chorus spelling `Allelluia` → `Alleluia` (both occurrences), matching the note's quoted text and the spelling used everywhere else in the book.

### Hymn 64 `MIREMBE MWOYO MUTUKIRIVU`

Verse 3, second paragraph replaced:

```
was:                                now:
Obusungu, obuseegu, obulimba        Obusungu, obuseegu, obulimba
N'obukuusa obwo, tubireke           N'obujja obwo tubireke
Ekitiibwe otuwe n'enyonta,          Ekitiibwa kye kibune wonna
Enyingi enno ey'obutukirivu.        Essanyu lyo litubukale.
```

Verse 4 replaced entirely with the four given lines; everything after it (the old verse 4's two paragraphs and the trailing unnumbered repeat) deleted:

```
4. Mirembe Mwoyo Mutukirivu
Katonda waffe omwagalwa;
Nnyini buyinza n'obutukuvu,
Ggwe Kabaka, Ggwe Kabaka.
```

Song now has 4 verses.

### Hymn 65 `MWOYO MUTUKIRIVU KATONDA OWA MAZIMA`

Verse 5 (`Ayi Mwoyo Mutukirivu / Ggwe Katonda owa Mazima…`) deleted; verse 6 renumbered to 5. Song now has 5 verses.

### Hymn 66 `AYI KATONDA PATRI KABAKA`

Verse 4 (`Ayi Kitaawe wa Yezu Kabaka…`) deleted. Song now has 3 verses.

---

## 3. Open items

### Awaiting your decision

**Item 2 — title colour.** The header bar behind the song title is currently **white**, so white text would be invisible as-is. Two ways to honour *"white but bold, bigger"*:

- **Option A** — give the header bar the app blue (`#2E62A6`), title white + bold + larger, icons tinted white. Literally what the note asks, and the title pops.
- **Option B** — keep the white header, make the title black/dark, bold and larger. Fixes "faint" without a colour-scheme change.

### Blocked

**Hymn 44** — needs the new version of the song text.

### Not yet started

- **Item 3** — table of contents for all songs.
- **Item 4** — songs grouped by category, plus font size control. Note: a font size control (16–22px slider with A−/A+ buttons, plus white/sepia/dark themes) **already exists** in the song reading screen. Worth confirming what v1.1.2 did differently before building anything new.
- **Item 5** — Hymn 2, remove the repeated number and name after verse 1.

### Needs attention before release

**Data corrections will not reach existing users.** `MainActivityBottom.java:65` only reloads from `hymns.json` when `isFirstLaunch() || isSynchronized()`:

```java
if (isFirstLaunch() || isSynchronized())
    connectToStorage();
```

Anyone who already has the app installed keeps their old database, corrections and all. A data-version check is needed — store a `data_version` preference, and force a reload when the bundled version is newer. Without it none of the above edits will appear on upgrade.

---

## 4. Spelling differences

In three places the handwritten note differed from spelling already used in the app. The app's existing spelling was kept, to avoid introducing typos into Luganda text. **All three are worth a second opinion from the author.**

| Hymn | Note says | Kept as | Why |
|---|---|---|---|
| 32, verse 2 line 4 | `Nobawo nga tolwala!` | `Nobaawo nga tolwala!` | Line already exists verbatim in the app |
| 64, verse 3 | `Essanyu lyo lutubukale.` | `Essanyu lyo litubukale.` | Line already exists verbatim in the app; `li-` agrees with `essanyu` |
| 64, verse 4 line 4 | `Gwe Kabaka, Gwe Kabaka.` | `Ggwe Kabaka, Ggwe Kabaka.` | `Ggwe` used throughout the book; the note's comma was adopted |

The source notes carry the caveat *"some Luganda spellings may need verification against the original"*, which is why these were flagged rather than applied.

---

## 5. Verification performed

- `hymns.json` parses cleanly; every entry has the expected schema (`id`, `title`, `content`, `number`, `category`) with a non-empty title and content.
- The file round-trips byte-identically through `json.dumps(ensure_ascii=False, indent=2)`, so the diff contains only intended lines — no reformatting noise.
- Home Songs audited: **1–73, no duplicates, no gaps.**
- Verse counts the app will display (via `Utils.findLargestNumber`) checked against the note for every restructured song — 17→5, 30→4, 32→5, 64→4, 65→5, 66→3. All correct.
- `./gradlew :app:compileDebugJavaWithJavac` passes.
