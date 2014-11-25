#!/bin/bash
for i in original/*; do 
  sox $i -t wav - vol 0.5 fade 0 1.0 0.4 | lame --abr 64 - processed/$(basename ${i%%.wav}).mp3
done
