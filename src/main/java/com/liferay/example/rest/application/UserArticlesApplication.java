package com.liferay.example.rest.application;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.liferay.example.rest.exception.JsonProcessingExceptionMapper;
import com.liferay.example.rest.exception.LiferayRestExampleException;
import com.liferay.example.rest.exception.LiferayRestExampleExceptionMapper;
import com.liferay.example.rest.exception.PortalExceptionMapper;
import com.liferay.example.rest.utils.Helper;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.SAXReaderUtil;
import org.osgi.service.component.annotations.Reference;

/**
 * @author angelcazares
 */
@ApplicationPath("/articles")
@Component(immediate = true, service = Application.class)
public class UserArticlesApplication extends Application {

	public Set<Object> getSingletons() {
		return Collections.<Object>singleton(this);
	}

	private static Properties APP_MESSAGES;
	private static String RANDOM_STRUCTURE_KEY;
	private static long SCOPE_GROUP_ID;
	private static Log _log = LogFactoryUtil.getLog(UserArticlesApplication.class.getName());

	@Reference
	JournalArticleLocalService journalArticleLocalService;

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		/* Classes to wrap any exception into a JSON response */
		classes.add(PortalExceptionMapper.class);
		classes.add(JsonProcessingExceptionMapper.class);
		classes.add(LiferayRestExampleExceptionMapper.class);
		/* add your additional JAX-RS classes here */
		return classes;
	}

	@GET
	@Path("/")
	@Produces("application/json")
	/***
	 * Get the content of the journal articles that were created using the BASIC
	 * WEB CONTENT Structure Validates if the User has permissions to view each
	 * article before returning the data.
	 * 
	 * @param request
	 * @return JSON array with the journal's article content
	 * @throws Exception
	 */
	public String getUserArticles(@Context HttpServletRequest request) throws Exception {

		/* Structure Key and Group Id could be obtained from the request */
		RANDOM_STRUCTURE_KEY = "BASIC-WEB-CONTENT";
		SCOPE_GROUP_ID = 20143L;
		APP_MESSAGES = new Helper().loadResourceBundled("/configuration/LiferayRestExample.properties");

		if (_log.isInfoEnabled()) {
			_log.info(String.format("Getting the journals that were created with the sructure [%s], and group Id [%s]",
					RANDOM_STRUCTURE_KEY, SCOPE_GROUP_ID));
		}

		User user = null;
		String userid = request.getRemoteUser();

		// By default, if the user has not been authenticated in the portal, the
		// Guest user is retrieved.
		// However, it is important to validate the case if it is null
		if (userid != null) {
			user = UserLocalServiceUtil.getUser(new Long(userid));
		}
		if (user == null) {
			throw new LiferayRestExampleException(Response.Status.UNAUTHORIZED.getStatusCode(),
					(String) APP_MESSAGES.get("userNotFound"));
		}

		// Get permission checker to validate if a user can view the retrieved
		// articles
		PermissionChecker checker = null;
		checker = PermissionCheckerFactoryUtil.create(user);
		if (checker == null) {
			throw new LiferayRestExampleException(Response.Status.UNAUTHORIZED.getStatusCode(),
					(String) APP_MESSAGES.get("userPermissionValidation"));
		}

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode articleArray = mapper.createArrayNode();
		JournalArticle article = null;

		// Get the articles created using the structure key
		List<JournalArticle> articles = journalArticleLocalService.getArticlesByStructureId(SCOPE_GROUP_ID,
				RANDOM_STRUCTURE_KEY, -1, -1, null);

		for (int articleIndex = 0; articleIndex < articles.size(); articleIndex++) {
			article = articles.get(articleIndex);

			// Validates if the user has permission to view the article.
			if (checker.hasPermission(article.getGroupId(), JournalArticle.class.getName(),
					article.getResourcePrimKey(), ActionKeys.VIEW)) {
				articleArray.add(getContentFromArticle(article));
			}
		}

		// Generate the JSON representation
		return mapper.writeValueAsString(articleArray);
	}

	/**
	 * Returns the Jackson object for the article, if any error ocurres then
	 * returns an empty object
	 *
	 * @param article
	 *            The article information
	 * @return The Jackson object
	 * @throws DocumentException
	 * @throws PortalException
	 */
	private ObjectNode getContentFromArticle(JournalArticle article) throws DocumentException, PortalException {
		Document document;
		String root = "/root/dynamic-element";
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode contentNode = mapper.createObjectNode();

		if (article.getContent().equals("Hola")) {
			return contentNode;
		}

		// Get the document (XML format)
		document = SAXReaderUtil.read(article.getContent());

		// Extract the article data
		String strContent = document.valueOf(root + "[@name='content']").trim();

		// Creates the Jackson JSON structure
		contentNode.put("content", strContent);

		return contentNode;
	}

}