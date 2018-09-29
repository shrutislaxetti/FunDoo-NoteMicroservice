package com.bridgelabz.microserice.fundoonotes.note.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.bridgelabz.microserice.fundoonotes.note.exceptions.LinkInformationException;
import com.bridgelabz.microserice.fundoonotes.note.models.URLMetaData;

@Component
public class LinkInfoProvider {

	public URLMetaData getLinkInformation(String link) throws LinkInformationException {

		String description = null;
		String imageUrl = null;
		try {
			Document document = Jsoup.connect(link).get();
			description = document.select("meta[name=description]").first().attr("content");
			imageUrl = document.select("img[src~=(?i)\\.(png|jpe?g|gif)]").attr("src");
		} catch (IOException exception) {
			throw new LinkInformationException("Failed to fetch information");
		}

		URLMetaData info = new URLMetaData();
		info.setLink(link);
		info.setImageURL(imageUrl);
		info.setDescription(description);

		return info;
	}

	public List<URLMetaData> getAllLink(String description) throws LinkInformationException {
		String[] stringArray = description.split(" ");

		List<String> urlList = NoteUtility.getUrlList(stringArray);

		List<URLMetaData> urlInfoList = new ArrayList<>();
		for (int j = 0; j < urlList.size(); j++) {
			urlInfoList.add(getLinkInformation(urlList.get(j)));

		}

		return urlInfoList;

	}
}