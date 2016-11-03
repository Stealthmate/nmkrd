package com.stealthmatedev.navermini.UI.specific;

import com.stealthmatedev.navermini.UI.DetailsVisualizer;
import com.stealthmatedev.navermini.UI.specific.EntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.en.details.EnWordDetailsVisualizer;
import com.stealthmatedev.navermini.UI.specific.en.search.EnWordEntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.jp.details.JpKanjiDetailsVisualizer;
import com.stealthmatedev.navermini.UI.specific.jp.details.JpWordDetailsVisualizer;
import com.stealthmatedev.navermini.UI.specific.jp.search.JpKanjiEntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.jp.search.JpWordEntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.kr.search.KrExampleEntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.kr.search.KrWordEntryVisualizer;
import com.stealthmatedev.navermini.UI.specific.kr.details.KrDetailsVisualizer;
import com.stealthmatedev.navermini.data.Entry;
import com.stealthmatedev.navermini.data.en.EnWord;
import com.stealthmatedev.navermini.data.jp.JpKanji;
import com.stealthmatedev.navermini.data.jp.JpWord;
import com.stealthmatedev.navermini.data.kr.KrExample;
import com.stealthmatedev.navermini.data.kr.KrWord;

/**
 * Created by Stealthmate on 16/11/03 0003.
 */

public class EntryUIMapper {

    public enum EntryUI {
        KR_WORD(KrWord.class, KrWordEntryVisualizer.class, KrDetailsVisualizer.class),
        KR_EXAMPLE(KrExample.class, KrExampleEntryVisualizer.class, null),

        JP_WORD(JpWord.class, JpWordEntryVisualizer.class, JpWordDetailsVisualizer.class),
        JP_KANJI(JpKanji.class, JpKanjiEntryVisualizer.class, JpKanjiDetailsVisualizer.class),

        EN_WORD(EnWord.class, EnWordEntryVisualizer.class, EnWordDetailsVisualizer.class);


        public final Class<? extends Entry> entryClass;
        public final Class<? extends EntryVisualizer> entryVisualizerClass;
        public final Class<? extends DetailsVisualizer> detailsVisualizerClass;

        EntryUI(Class<? extends Entry> entryClass, Class<? extends EntryVisualizer> entryVisualizerClass, Class<? extends DetailsVisualizer> detailsVisualizerClass) {
            this.entryClass = entryClass;
            this.entryVisualizerClass = entryVisualizerClass;
            this.detailsVisualizerClass = detailsVisualizerClass;
        }
    }

    public static EntryUI forEntry(Entry entry) {
        if(entry.getClass().equals(KrWord.class)) return EntryUI.KR_WORD;
        if(entry.getClass().equals(KrExample.class)) return EntryUI.KR_EXAMPLE;
        if(entry.getClass().equals(JpWord.class)) return EntryUI.JP_WORD;
        if(entry.getClass().equals(JpKanji.class)) return EntryUI.JP_KANJI;
        if(entry.getClass().equals(EnWord.class)) return EntryUI.EN_WORD;

        return null;
    }

}
