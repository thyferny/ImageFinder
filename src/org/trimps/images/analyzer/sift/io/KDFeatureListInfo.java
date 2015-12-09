
package org.trimps.images.analyzer.sift.io;

import java.util.List;

import org.trimps.images.analyzer.sift.scale.KDFeaturePoint;


public class KDFeatureListInfo {
	private String imageFile;
    private List<KDFeaturePoint> list;
    private int width;
    private int height;
    
    public String getImageFile() {
        return imageFile;
    }
    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
    
    public List<KDFeaturePoint> getList() {
        return list;
    }
    public void setList(List<KDFeaturePoint> list) {
        this.list = list;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }    
}

