package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Table(name = "items")
public class SampleModel extends Model {
	// Define table fields
	@Column(name = "name")
	private String name;

	public SampleModel() {
		super();
	}

	// Parse model from JSON
	public SampleModel(JSONObject object){
		super();

		try {
			this.name = object.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public String getName() {
		return name;
	}

	// Record Finders
	public static SampleModel byId(long id) {
		return new Select().from(SampleModel.class).where("id = ?", id).executeSingle();
	}

	public static List<SampleModel> recentItems() {
		return new Select().from(SampleModel.class).orderBy("id DESC").limit("300").execute();
	}
}
