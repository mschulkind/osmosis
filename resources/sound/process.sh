#!/bin/bash
for i in original/*; do 
  sox $i -t wav - fade 0 1 | lame - processed/$(basename ${i%%.wav}).mp3
done
