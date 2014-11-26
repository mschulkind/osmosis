#!/bin/bash
OUT_DIR=../../resources/public/sound
MANIFEST=${OUT_DIR}/manifest.edn

echo "[" > $MANIFEST

mkdir -p $OUT_DIR
for i in *.wav; do 
  OUT_FILE_BASE=$(basename ${i%%.wav}).mp3

  sox $i -t wav - vol 0.5 fade 0 1.0 0.3 \
    | lame --abr 64 - ${OUT_DIR}/${OUT_FILE_BASE}

  echo "\"$OUT_FILE_BASE\"" >> $MANIFEST
done

echo "]" >> $MANIFEST
