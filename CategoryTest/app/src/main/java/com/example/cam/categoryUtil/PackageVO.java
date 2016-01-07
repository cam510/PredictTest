package com.example.cam.categoryUtil;

import java.io.Serializable;


public class PackageVO implements Serializable, Comparable<PackageVO> {
	private static final long serialVersionUID = 3487770984476040019L;
	
	public String appname = "";
	public String pname = "";
	public String versionName = "";
	public int versionCode = 0;
	public long firstInstallTime = 0;
	public long lastUndateTime = 0;
	public String dataDir = "";
	public int targetSdkVersion = 0;
	
	public String category = "unknow";
	public boolean systemApp = false;
	
	public long size = 0;
//	public Drawable icon;
//	public int iconResId;

    public String[] requestedPermissions;
    public long uninstalledDate;

	@Override
	public int compareTo(PackageVO another) {
		// TODO Auto-generated method stub
		return 0;
	}

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;

        PackageVO test = (PackageVO)obj;
        return (pname != null && pname.equals(test.pname));
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash;
        hash = 31 * hash + (null == pname ? 0 : pname.hashCode());
        return hash;
    }

    @Override
	public String toString() {
		return "PackageVO [App Label=" + appname + ", Package Name=" + pname + ", Version Name=" + versionName + ", System App=" + systemApp
				+ ", Version Code=" + versionCode + ", First Installed Time=" + firstInstallTime + "]";
    }
}
