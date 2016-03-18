package jp.thotta.oml.server.model;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import jp.thotta.oml.server.admin.PathManager;

public class ThresholdMap {
  int modelId;
  Map<String, Threshold> m;

  public ThresholdMap(int modelId) {
    this.modelId = modelId;
    m = new HashMap<String, Threshold>();
  }

  public boolean containsKey(String k) {
    return m.containsKey(k);
  }

  public Threshold get(String k) {
    return m.get(k);
  }

  public void put(String k, Threshold v) {
    m.put(k, v);
  }

  public void read() {
    String filename = PathManager.thresholdFile(this.modelId);
    File file = new File(filename);
    try {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = "";
      while ((line = br.readLine()) != null) {
        Threshold t = new Threshold();
        t.readLine(line);
        m.put(t.getLabel(), t);
      }
      br.close();
    } catch(FileNotFoundException e) {
      return;
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void save() {
    String filename = PathManager.thresholdFile(this.modelId);
    File file = new File(filename);
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(file));
      for(String label : m.keySet()) {
        bw.write(m.get(label).getLine());
        bw.newLine();
      }
      bw.close();
    } catch(Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  }
}
