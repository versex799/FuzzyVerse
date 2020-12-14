package fuzzyverse;

import javax.print.DocFlavor;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;


public class Wordlist {

    private String _wordlist;
    private String _nextLine;
    private BufferedReader _bufferedReader;

    public String CurrentLine;

    public Wordlist(String wordlist)
    {
        _wordlist = wordlist;
    }


    public void Import() throws IOException {
        FileReader fileReader = new FileReader(_wordlist);

        _bufferedReader = new BufferedReader(fileReader);
        CurrentLine = _bufferedReader.readLine();
        _nextLine = CurrentLine;
    }

    public String NextLine() throws IOException {

        CurrentLine = _nextLine;
        _nextLine = _bufferedReader.readLine();

        return CurrentLine;
    }

    public boolean HasNextLine()
    {
        return _nextLine != null;
    }
}
