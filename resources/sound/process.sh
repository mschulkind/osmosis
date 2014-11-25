#!/bin/bash
for i in original/*; do 
  sox $i -t wav - fade 0 1.2 0.4 | lame - processed/$(basename ${i%%.wav}).mp3
done
mp3gain -r -k processed/*
