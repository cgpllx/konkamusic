package com.konka.music.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.konka.music.pojo.AllLableInfos;
import com.konka.music.pojo.BigLabelInfo;
import com.konka.music.pojo.MusicInfo;
import com.konka.music.pojo.SmallLabelInfo;
import com.konka.music.wedget.MusicApplication;
import com.kubeiwu.baseclass.util.JsonUtil;
import com.kubeiwu.commontool.khttp.KRequestQueueManager;
import com.kubeiwu.commontool.khttp.Request;
import com.kubeiwu.commontool.khttp.Response.ErrorListener;
import com.kubeiwu.commontool.khttp.Response.Listener;
import com.kubeiwu.commontool.khttp.exception.VolleyError;
import com.kubeiwu.commontool.khttp.requestimpl.JsonArrayRequest;

public class RequestUtil {
	/**
	 * http://music.konkacloud.cn/Client/?getTags
	 * 
	 * @param loader
	 */
	public static void handleLableFromNet2Db(  final BlockingDeque<AllLableInfos> queue) {
		

		final AllLableInfos allLableInfos = new AllLableInfos();
		Request<JSONArray> request = new JsonArrayRequest(Assist.BIGLABLE_URL, new Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				ArrayList<BigLabelInfo> bigLabelInfos = new ArrayList<BigLabelInfo>();
				ArrayList<SmallLabelInfo> smallLabelInfos = new ArrayList<SmallLabelInfo>();
				BigLabelInfo bigLabelInfo;
				SmallLabelInfo smallLabelInfo;
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject jsonObject = response.getJSONObject(i);
						bigLabelInfo = new BigLabelInfo(JsonUtil.getInt(jsonObject, "id"),//
								JsonUtil.getString(jsonObject, "name"));
						String smallLabelInfoJsonString = JsonUtil.getString(jsonObject, "tags");
						if (!TextUtils.isEmpty(smallLabelInfoJsonString)) {
							JSONArray smallLabelInfoJsonArray = new JSONArray(smallLabelInfoJsonString);
							for (int j = 0; j < smallLabelInfoJsonArray.length(); j++) {
								JSONObject smallLabelInfoJsonObject = smallLabelInfoJsonArray.getJSONObject(j);

								smallLabelInfo = new SmallLabelInfo(JsonUtil.getInt(smallLabelInfoJsonObject, "id"), //
										JsonUtil.getString(smallLabelInfoJsonObject, "name"),//
										bigLabelInfo.getId(), JsonUtil.getString(smallLabelInfoJsonObject, "image"));
								smallLabelInfos.add(smallLabelInfo);
							}
						}
						bigLabelInfos.add(bigLabelInfo);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				MusicApplication.mKCommonToolDb.insertOrReplaceAll(bigLabelInfos);
				MusicApplication.mKCommonToolDb.insertOrReplaceAll(smallLabelInfos);
				allLableInfos.setBigLabelInfos(bigLabelInfos);
				allLableInfos.setSmallLabelInfos(smallLabelInfos);
				queue.add(allLableInfos);

			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				queue.add(allLableInfos);
			}
		});
		KRequestQueueManager.getRequestQueue().add(request);

	}

	/**
	 * http://music.konkacloud.cn/Client/?music_Search.html&key=%E5%B0%8F%E8%8B%B9%E6%9E%9C
	 */
	public static void handleSearchFromNet1(String searchKey, final BlockingDeque<ArrayList<MusicInfo>> queue) {

		try {
			searchKey = URLEncoder.encode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String url = String.format(Assist.SEARCHMUSIC, searchKey);
		final ArrayList<MusicInfo> musicinfos = new ArrayList<MusicInfo>();
		Request<JSONArray> request = new JsonArrayRequest(url, new Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {

				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject jsonObject = response.getJSONObject(i);
						MusicInfo musicInfo = Json2MusicInfo.parse(jsonObject);
						musicinfos.add(musicInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				queue.add(musicinfos);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				queue.add(musicinfos);
			}
		});
		KRequestQueueManager.getRequestQueue().add(request);
	}

	/**
	 * 根据地址获取ArrayList<MusicInfo>>
	 */
	public static void handleMusicInfosFromNet(String baseUri, final BlockingDeque<ArrayList<MusicInfo>> queue) {
		final ArrayList<MusicInfo> musicinfos = new ArrayList<MusicInfo>();
		Request<JSONArray> request = new JsonArrayRequest(baseUri, new Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				for (int i = 0; i < response.length(); i++) {
					try {
						JSONObject jsonObject = response.getJSONObject(i);
						MusicInfo musicInfo = Json2MusicInfo.parse(jsonObject);
						musicinfos.add(musicInfo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				queue.add(musicinfos);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				queue.add(musicinfos);
			}
		});
		KRequestQueueManager.getRequestQueue().add(request);
	}
}
