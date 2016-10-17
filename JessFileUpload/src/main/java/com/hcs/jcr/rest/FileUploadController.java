package com.hcs.jcr.rest;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application file upload requests
 */
@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("jsondata") String jsonInString,
			@RequestParam("file") MultipartFile file) {

		Session session1 = null;
		if (!file.isEmpty()) {
			try {
				ObjectMapper mapper = new ObjectMapper();

				MetaData metaData = mapper.readValue(jsonInString, MetaData.class);

				InputStream stream = new BufferedInputStream(file.getInputStream());

				Repository repository = JcrUtils.getRepository("http://localhost:7001/jackrabbit/server");
				session1 = repository.login(new SimpleCredentials("weblogic", "Weblogic1".toCharArray()));
				Node root = session1.getRootNode();

				Node parentPath = root.getNode(metaData.getParentPath());

				Node fileName = parentPath.addNode(metaData.getLabel(), "nt:file");

				Node content = fileName.addNode("jcr:content", "nt:resource");
				Binary binary = session1.getValueFactory().createBinary(stream);
				content.setProperty("jcr:data", binary);
				content.setProperty("jcr:mimeType", "application/pdf");

				session1.save();

				return "You successfully uploaded file=" + fileName;
			} catch (Exception e) {
				return "You failed to upload  => " + e.getMessage();
			}
		} else {
			return "You failed to upload because the file was empty.";
		}
	}

	/**
	 * Upload single file using Spring Controller
	 */

	@RequestMapping(value = "/updateFile", method = RequestMethod.POST)
	public @ResponseBody String updateFile(@RequestParam("name") String name, @RequestParam("dirPath") String dirPath,
			@RequestParam("file") MultipartFile file) {

		Session session1 = null;
		if (!file.isEmpty()) {
			try {

				InputStream stream = new BufferedInputStream(file.getInputStream());

				Repository repository = JcrUtils.getRepository("http://localhost:7001/jackrabbit/server");
				session1 = repository.login(new SimpleCredentials("weblogic", "Weblogic1".toCharArray()));
				Node root = session1.getRootNode();

				Node dirPath1 = root.getNode(dirPath);

				Node test2 = dirPath1.getNode(name);
				test2.getNode("jcr:content").remove();
				;

				Node content = test2.addNode("jcr:content", "nt:resource");
				Binary binary = session1.getValueFactory().createBinary(stream);
				content.setProperty("jcr:data", binary);
				content.setProperty("jcr:mimeType", "application/pdf");

				session1.save();

				return "You successfully uploaded file=" + name;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	/**
	 * Upload single file using Spring Controller
	 */

	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public @ResponseBody String deleteFile(@RequestParam("name") String name, @RequestParam("dirPath") String dirPath,
			@RequestParam("file") MultipartFile file) {

		Session session1 = null;
		if (!file.isEmpty()) {
			try {

				InputStream stream = new BufferedInputStream(file.getInputStream());

				Repository repository = JcrUtils.getRepository("http://localhost:7001/jackrabbit/server");
				session1 = repository.login(new SimpleCredentials("weblogic", "Weblogic1".toCharArray()));
				Node root = session1.getRootNode();

				Node dirPath1 = root.getNode(dirPath);

				dirPath1.getNode(name).remove();

				session1.save();

				return "You successfully uploaded file=" + name;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	/**
	 * Upload single file using Spring Controller
	 */

	@RequestMapping(value = "/createDirectory", method = RequestMethod.POST)
	public @ResponseBody String createDirectory(@RequestParam("name") String name,
			@RequestParam("dirPath") String dirPath, @RequestParam("file") MultipartFile file) {

		Session session1 = null;
		if (!file.isEmpty()) {
			try {

				InputStream stream = new BufferedInputStream(file.getInputStream());

				Repository repository = JcrUtils.getRepository("http://localhost:7001/jackrabbit/server");
				session1 = repository.login(new SimpleCredentials("weblogic", "Weblogic1".toCharArray()));
				Node root = session1.getRootNode();

				String[] paths = dirPath.split("/");

				Node dirPath1 = root.addNode(paths[0]);
				session1.save();
				Node dirPath2 = dirPath1.addNode(paths[1]);

				session1.save();

				return "You successfully uploaded file=" + name;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

}
