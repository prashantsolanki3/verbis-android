package com.blackshift.verbis.rest.model.wordy;

import org.parceler.Parcel;

import java.io.File;

@Parcel
public class TextConfig{
        File font;
        int size;
        String color;

        public File getFont() {
            return font;
        }

        public TextConfig setFont(File font) {
            this.font = font;
            return this;
        }

        public int getSize() {
            return size;
        }

        public TextConfig setSize(int size) {
            this.size = size;
            return this;
        }

        public String getColor() {
            return color;
        }

        public TextConfig setColor(String color) {
            this.color = color;
            return this;
        }
    }