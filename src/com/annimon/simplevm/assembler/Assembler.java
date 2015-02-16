package com.annimon.simplevm.assembler;

import com.annimon.simplevm.Program;
import com.annimon.simplevm.utils.FileUtils;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author aNNiMON
 */
public class Assembler {
    
    public static Program fromInputStream(InputStream is) throws IOException {
        return Assembler.fromString(FileUtils.readContents(is));
    }
    
    public static Program fromString(String text) {
        return Parser.parse(Tokenizer.tokenize(text));
    }
    
    
}
