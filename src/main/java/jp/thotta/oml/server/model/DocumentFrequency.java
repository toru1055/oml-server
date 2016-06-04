package jp.thotta.oml.server.model;

import jp.thotta.oml.server.admin.PathManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DocumentFrequency {
    int modelId;
    Map<String, Integer> m;

    public DocumentFrequency(int modelId) {
        this.modelId = modelId;
        m = new HashMap<String, Integer>();
    }

    public boolean containsKey(String k) {
        return m.containsKey(k);
    }

    public Integer get(String k) {
        return m.get(k);
    }

    public void put(String k, Integer v) {
        m.put(k, v);
    }

    public void read() {
        String filename = PathManager.dfFile(this.modelId);
        File file = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] s = line.split("\t", 2);
                String term = s[0];
                Integer freq = Integer.parseInt(s[1]);
                m.put(term, freq);
            }
            br.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void save() {
        String filename = PathManager.dfFile(this.modelId);
        File file = new File(filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String term : m.keySet()) {
                bw.write(term + "\t" + m.get(term));
                bw.newLine();
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
