package com.konka.music.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;

import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.Singer;
import com.kubeiwu.baseclass.util.JsonUtil;
import com.kubeiwu.baseclass.util.KLog;

public class Net {

	public static final String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";

	public static ArrayList<MusicInfo> getMusicInfoArray1(String baseUri) {
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
		HttpParams params = httpClient.getParams();
		ArrayList<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		HttpGet httpGet = new HttpGet(baseUri);
		try {
			HttpConnectionParams.setConnectionTimeout(params, 3000); // ///////
			HttpConnectionParams.setSoTimeout(params, 3000); // //////////

			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				String result = EntityUtils.toString(resEntity, HTTP.UTF_8);
				JSONArray response = new JSONArray(result);
				if (response == null || response.length() == 0) {
					return musicInfos;
				}
				for (int i = 0; i < response.length(); i++) {
					JSONObject jsonObject;
					try {
						jsonObject = response.getJSONObject(i);
						MusicInfo musicInfo = Json2MusicInfo.parse(jsonObject);
						musicInfos.add(musicInfo);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return musicInfos;
	}

	public static ArrayList<Singer> getHotSingerArray(String baseUri) {
		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
		HttpParams params = httpClient.getParams();
		ArrayList<Singer> singers = new ArrayList<Singer>();
		HttpGet httpGet = new HttpGet(baseUri);
		try {
			HttpConnectionParams.setConnectionTimeout(params, 3000); // ///////
			HttpConnectionParams.setSoTimeout(params, 3000); // //////////

			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				String result = EntityUtils.toString(resEntity, HTTP.UTF_8);
				JSONArray response = new JSONArray(result);
				if (response == null || response.length() == 0) {
					return singers;
				}
				for (int i = 0; i < response.length(); i++) {
					JSONObject jsonObject;
					try {
						// 解析完毕
						jsonObject = response.getJSONObject(i);
						Singer singer = new Singer();
						singer.setId(JsonUtil.getInt(jsonObject, "id"));
						singer.setName(JsonUtil.getString(jsonObject, "name"));
						singer.setImageurl(JsonUtil.getString(jsonObject, "Pic"));
						singers.add(singer);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
		return singers;
	}

	/**
	 * @author wangxu
	 * @description 获取最新版本号
	 * */
	public static String getLatestVersionCode(String uri, int currentVersion) {
		HttpURLConnection cn = null;
		URL url = null;
		try {
			url = new URL(uri);
			cn = (HttpURLConnection) url.openConnection();
			cn.setConnectTimeout(10000);
			cn.setReadTimeout(20000);
			cn.connect();
			InputStream stream = cn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			JSONObject jsonObject = new JSONObject(builder.toString());
			int latestVersion = jsonObject.getInt("version");
			String path = jsonObject.getString("path");
			if (latestVersion > currentVersion) {
				KLog.v("wangxu", "Net->getLatestVersionCode->upgrade path is " + path);
				return path;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cn != null)
				cn.disconnect();
		}
		return null;
	}

	/**
	 * 提交反馈信息
	 * 
	 * @return true 反馈成功 false 反馈失败
	 * */
	public static boolean postFeedbackMessage(String uri, String title, String content) {

		AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpParams httpParams = httpClient.getParams();

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Feedback[title]", title));
		params.add(new BasicNameValuePair("Feedback[content]", content));

		try {
			HttpPost httpPost = new HttpPost(uri);
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpEntity httpEntity = new UrlEncodedFormEntity(params);
			httpPost.setEntity(httpEntity);
			HttpResponse response = httpClient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity);
				if (result.equals("success"))
					return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			KLog.e("wangxu", "Net->postFeedbackMessage->error: " + e);
			return false;
		} finally {
			if (httpClient != null)
				httpClient.close();
		}

		return false;
	}
}
