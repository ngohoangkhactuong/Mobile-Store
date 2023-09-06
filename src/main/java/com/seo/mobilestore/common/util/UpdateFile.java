package com.seo.mobilestore.common.util;

import com.seo.mobilestore.common.FileUpload;

public interface UpdateFile {


	void update(FileUpload fileUp);
	void uploadCV(FileUpload fileUpload);
	void uploadCVApplication(FileUpload fileUpload);
	void uploadExcel(FileUpload fileUp);
	void deleteFile(String url);
}
