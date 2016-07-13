package com.zzl.mobileplayer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 模拟歌词加载模块
 * @author Administrator
 *
 */
public class LyricLoader {
	private static String LYRIC_DIR = "/mnt/sdcard/test/Music/";
	public static File loadLyricFile(String audioName){
		File file = new File(LYRIC_DIR, StringUtil.formatAudioName(audioName)+".lrc");
		if(!file.exists()){
			file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), "ningxia.lrc");
			
		}
		return file;
	}
	
	 
}
