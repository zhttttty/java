package com.zhizhuotec.common.utils;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;

import com.zhizhuotec.common.pojo.Constant;

import main.java.com.UpYun;
import main.java.com.UpYun.FolderItem;

public class UpUtils {
	/**
	 * 服务名称
	 */
	private static final String BUCKET_NAME = "file-resources";
	/**
	 * 操作员名称
	 */
	private static final String OPERATOR_NAME = "zhizhuotec";
	/**
	 * 操作员密码
	 */
	private static final String OPERATOR_PWD = "zztec0330";
	/**
	 * UpYun 存储接口
	 */
	private static UpYun upYun = null;
	/**
	 * 路径的分割符
	 */
	public final static String SEPARATOR = "/";
	/**
	 * 编码
	 */
	private final static String UTF8 = "UTF-8";
	/**
	 * 分隔符
	 */
	private final static String DECOLLATOR = "|";
	/**
	 * 换行符
	 */
	private final static String ENTER = "\n";
	/**
	 * 文件类型
	 */
	private final static String SUFFIX = ".txt";

	static {
		upYun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
		upYun.setDebug(true);
		upYun.setTimeout(60);
		upYun.setApiDomain(UpYun.ED_AUTO);
	}

	/**
	 * @param filePath
	 *            文件路径（包含文件名）
	 * @return byte[] or null
	 */
	public static byte[] download(String filePath) {
		try {
			File tempFile = File.createTempFile(Long.toString(System.currentTimeMillis()), ".tmp");
			if (upYun.readFile(filePath, tempFile)) {
				InputStream in = new FileInputStream(tempFile);
				byte[] b = new byte[in.available()];
				in.read(b);
				in.close();
				tempFile.delete();
				return b;
			}
		} catch (Exception e) {
			Utils.logger("ERROR", "java.io.FileNotFoundException：" + filePath);
		}
		return null;
	}

	/**
	 * @param uId
	 * @param timestamp
	 *            时间戳
	 * @param value
	 *            值
	 * @return true or false
	 */
	public static boolean writeFileByLine(String uId, Object timestamp, String value) {
		try {
			String filePath = Constant.BASE_PATH.getValue() + SEPARATOR + Constant.ECG_FOLDER.getValue() + SEPARATOR
					+ uId + SEPARATOR + new DateTime(timestamp).toString("yyyyMMdd") + SUFFIX;
			value = timestamp + DECOLLATOR + value + ENTER;
			byte[] b = download(filePath);
			if (b != null) {
				String oldValue = new String(b, UTF8);
				if (oldValue.indexOf(value) == -1) {
					value = oldValue + value;
				}
			}
			boolean re = upYun.writeFile(filePath, value, true);
			if (!re) {
				Utils.logger("WARN", "Ecg write failure in timestamp：" + timestamp);
			}
			return re;
		} catch (Exception e) {
			Utils.logger("ERROR", "Server exception：", e);
		}
		return false;
	}

	/**
	 * @param uId
	 * @param beginFileName
	 *            开始时间
	 * @return
	 */
	public static Map<Long, Object> readFileByLine(String uId, int beginFileName) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		List<FolderItem> item = null;
		String filePath = Constant.BASE_PATH.getValue() + SEPARATOR + Constant.ECG_FOLDER.getValue() + SEPARATOR + uId
				+ SEPARATOR;
		try {
			item = upYun.readDir(filePath);
		} catch (Exception e) {

		}
		if (item != null) {
			int itemSize = item.size();
			for (int i = 0; i < itemSize; i++) {
				String name = item.get(i).name;
				if (Integer.valueOf(name.substring(0, name.indexOf("."))) >= beginFileName) {
					byte[] datas = download(filePath + name);
					int dataSize = datas.length;
					int end = 0;
					byte[] line = new byte[0];
					for (int j = 0; j < dataSize; j++) {
						if (datas[j] == 10) { // 换行符
							int lineSize = j - end;
							line = new byte[lineSize];
							System.arraycopy(datas, end, line, 0, j - end);
							end = j + 1;
							for (int k = 0; k < lineSize; k++) {
								if (line[k] == 124) { // 分隔符
									byte[] time = new byte[k];
									byte[] value = new byte[lineSize - k - 1];
									System.arraycopy(line, 0, time, 0, k);
									System.arraycopy(line, k + 1, value, 0, lineSize - k - 1);
									try {
										map.put(Long.valueOf(new String(time, UTF8)), new String(value, UTF8));
									} catch (Exception e) {
										Utils.logger("ERROR", "Server exception：", e);
									}
									break;
								}
							}
						}
					}
				}

			}
		}
		return map;
	}

	/**
	 * 
	 * @param uId
	 * @param beginFileName
	 *            开始时间
	 * @param endFileName
	 *            结束时间
	 * @return
	 */
	public static Map<Long, Object> readFileByLine(String uId, int beginFileName, int endFileName) {
		Map<Long, Object> map = new HashMap<Long, Object>();
		List<FolderItem> item = null;
		String filePath = Constant.BASE_PATH.getValue() + SEPARATOR + Constant.ECG_FOLDER.getValue() + SEPARATOR + uId
				+ SEPARATOR;
		try {
			item = upYun.readDir(filePath);
		} catch (Exception e) {
			Utils.logger("ERROR", "java.io.FileNotFoundException：" + filePath);
		}
		if (item != null) {
			int itemSize = item.size();
			for (int i = 0; i < itemSize; i++) {
				String name = item.get(i).name;
				if (Integer.valueOf(name.substring(0, name.indexOf("."))) >= beginFileName
						&& Integer.valueOf(name.substring(0, name.indexOf("."))) <= endFileName) {
					byte[] datas = download(filePath + name);
					int dataSize = datas.length;
					int end = 0;
					byte[] line = new byte[0];
					for (int j = 0; j < dataSize; j++) {
						if (datas[j] == 10) { // 换行符
							int lineSize = j - end;
							line = new byte[lineSize];
							System.arraycopy(datas, end, line, 0, j - end);
							end = j + 1;
							for (int k = 0; k < lineSize; k++) {
								if (line[k] == 124) { // 分隔符
									byte[] time = new byte[k];
									byte[] value = new byte[lineSize - k - 1];
									System.arraycopy(line, 0, time, 0, k);
									System.arraycopy(line, k + 1, value, 0, lineSize - k - 1);
									try {
										map.put(Long.valueOf(new String(time, UTF8)), new String(value, UTF8));
									} catch (Exception e) {
										Utils.logger("ERROR", "Server exception：", e);
									}
									break;
								}
							}
						}
					}
				}

			}
		}
		return map;
	}

	/**
	 * @param basePath
	 *            目录
	 * @param folder
	 *            文件夹
	 * @return 返回最新文件
	 */
	public static String findLastModifiedFile(Object basePath, Object folder) {
		String filePath = basePath + SEPARATOR + folder + SEPARATOR;
		List<FolderItem> item = null;
		try {
			item = upYun.readDir(filePath);
		} catch (Exception e) {
			Utils.logger("ERROR", "Server exception：", e);
		}
		long lastModified = 0;
		String lastModifiedFileName = null;
		if (item != null) {

			for (FolderItem file : item) {
				String name = file.name;
				long date = 0;
				try {
					Map<String, String> info = upYun.getFileInfo(filePath + name);
					date = Long.valueOf(info.get("date"));
				} catch (Exception e) {
					Utils.logger("ERROR", "Server exception：", e);
					return null;
				}
				if (date > lastModified) {
					lastModified = date;
					lastModifiedFileName = name;
				}
			}
		}
		return lastModifiedFileName;
	}

	/**
	 * @param data
	 *            Base64 String
	 * @param folder
	 *            上传的文件夹
	 * @param fileName
	 *            上传的文件名
	 * @return true or false
	 */
	public static boolean uploadToBase64(String data, Object folder, Object fileName) {
		if (data != null) {
			try {
				Decoder decoder = Base64.getMimeDecoder();
				// Base64解码
				byte[] b = decoder.decode(data);
				// 生成文件
				String path = Constant.BASE_PATH.getValue() + SEPARATOR + folder + SEPARATOR + fileName;
				return upYun.writeFile(path, b, true);
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
		}
		return false;
	}

	//
	// public static boolean resumeUpload(String dates, Object folder, Object
	// fileName) {
	// final ResumeUploader resume = new ResumeUploader(BUCKET_NAME,
	// OPERATOR_NAME, OPERATOR_PWD);
	// //设置上传进度监听
	// resume.setOnProgressListener(new ResumeUploader.OnProgressListener() {
	// public void onProgress(int index, int total) {
	// System.out.println(index + "::" + total + "::" + index * 100 / total +
	// "%");
	// }
	// });
	//
	// //设置 MD5 校验
	// resume.setCheckMD5(true);
	//
	// new Thread() {
	// @Override
	// public void run() {
	// super.run();
	// try {
	// try {
	// System.out.println("first:" + resume.upload(SAMPLE_PIC_FILE, UPLOAD_PATH,
	// null));
	// } catch (UpException e) {
	// e.printStackTrace();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }.start();
	//
	// Thread.sleep(2000);
	//
	// resume.interrupt(new ResumeUploader.OnInterruptListener() {
	// public void OnInterrupt(boolean interrupted) {
	//
	// System.out.println("interrupted:" + interrupted);
	//
	// if (interrupted) {
	// new Thread() {
	// @Override
	// public void run() {
	// super.run();
	// try {
	// System.out.println("second:" + resume.upload(SAMPLE_PIC_FILE,
	// UPLOAD_PATH, null));
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (UpException e) {
	// e.printStackTrace();
	// }
	// }
	// }.start();
	// }
	// }
	// });
	//
	//
	// return false;
	// }
}
