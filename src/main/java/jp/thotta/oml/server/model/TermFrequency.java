package jp.thotta.oml.server.model;

import jp.thotta.oml.server.admin.PathManager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TermFrequency {
    int modelId;
    Map<String, Map<String, Integer>> m;

    public TermFrequency(int modelId) {
        this.modelId = modelId;
        m = new HashMap<String, Map<String, Integer>>();
    }

    public boolean containsKey(String k) {
        return m.containsKey(k);
    }

    public Map<String, Integer> get(String k) {
        return m.get(k);
    }

    public void put(String k, Map<String, Integer> v) {
        m.put(k, v);
    }

    public Set<String> keySet() {
        return m.keySet();
    }

    public void read() {
        String filename = PathManager.tfFile(this.modelId);
        File file = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] s = line.split("\t", 3);
                String label = s[0];
                String term = s[1];
                Integer freq = Integer.parseInt(s[2]);
                Map<String, Integer> tf;
                if (m.containsKey(label)) {
                    tf = m.get(label);
                } else {
                    tf = new HashMap<String, Integer>();
                    m.put(label, tf);
                }
                tf.put(term, freq);
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
        String filename = PathManager.tfFile(this.modelId);
        File file = new File(filename);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String label : m.keySet()) {
                Map<String, Integer> tf = m.get(label);
                for (String term : tf.keySet()) {
                    bw.write(label + "\t" + term + "\t" + tf.get(term));
                    bw.newLine();
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
