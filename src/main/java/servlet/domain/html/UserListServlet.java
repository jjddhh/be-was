package servlet.domain.html;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import container.annotation.MyMapping;
import container.annotation.ResponseBody;
import db.UserDatabase;
import model.user.User;
import servlet.Servlet;
import session.SessionStorage;
import webserver.http.HttpRequest;

@MyMapping(url = "/user/list.html")
@ResponseBody
public class UserListServlet implements Servlet {

	@Override
	public String execute(HttpRequest httpRequest) {
		Map<String, String> cookies = httpRequest.getCookies();
		String sid = cookies.get("sid");

		StringBuilder htmlBuilder = new StringBuilder();

		if (isLoginUser(sid)) {
			buildUserListHtml(htmlBuilder);
			System.out.println(htmlBuilder);
			return htmlBuilder.toString();
		}

		buildLoginHtml(htmlBuilder);
		return htmlBuilder.toString();
	}

	private static void buildUserListHtml(StringBuilder htmlBuilder) {
		htmlBuilder.append("<!DOCTYPE html>\n");
		htmlBuilder.append("<html lang=\"kr\">\n");
		htmlBuilder.append("<head>\n");
		htmlBuilder.append("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
		htmlBuilder.append("    <meta charset=\"utf-8\">\n");
		htmlBuilder.append("    <title>SLiPP Java Web Programming</title>\n");
		htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
		htmlBuilder.append("    <link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n");
		htmlBuilder.append("    <!--[if lt IE 9]>\n");
		htmlBuilder.append("    <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n");
		htmlBuilder.append("    <![endif]-->\n");
		htmlBuilder.append("    <link href=\"../css/styles.css\" rel=\"stylesheet\">\n");
		htmlBuilder.append("</head>\n");
		htmlBuilder.append("<body>\n");
		htmlBuilder.append("<nav class=\"navbar navbar-fixed-top header\">\n");
		htmlBuilder.append("    <div class=\"col-md-12\">\n");
		htmlBuilder.append("        <div class=\"navbar-header\">\n");
		htmlBuilder.append("            <a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n");
		htmlBuilder.append("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n");
		htmlBuilder.append("                <i class=\"glyphicon glyphicon-search\"></i>\n");
		htmlBuilder.append("            </button>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n");
		htmlBuilder.append("            <form class=\"navbar-form pull-left\">\n");
		htmlBuilder.append("                <div class=\"input-group\" style=\"max-width:470px;\">\n");
		htmlBuilder.append("                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n");
		htmlBuilder.append("                    <div class=\"input-group-btn\">\n");
		htmlBuilder.append("                        <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n");
		htmlBuilder.append("                    </div>\n");
		htmlBuilder.append("                </div>\n");
		htmlBuilder.append("            </form>\n");
		htmlBuilder.append("            <ul class=\"nav navbar-nav navbar-right\">\n");
		htmlBuilder.append("                <li>\n");
		htmlBuilder.append("                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n");
		htmlBuilder.append("                    <ul class=\"dropdown-menu\">\n");
		htmlBuilder.append("                        <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n");
		htmlBuilder.append("                        <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n");
		htmlBuilder.append("                    </ul>\n");
		htmlBuilder.append("                </li>\n");
		htmlBuilder.append("                <li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</nav>\n");
		htmlBuilder.append("<div class=\"navbar navbar-default\" id=\"subnav\">\n");
		htmlBuilder.append("    <div class=\"col-md-12\">\n");
		htmlBuilder.append("        <div class=\"navbar-header\">\n");
		htmlBuilder.append("            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n");
		htmlBuilder.append("            <ul class=\"nav dropdown-menu\">\n");
		htmlBuilder.append("                <li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n");
		htmlBuilder.append("                <li class=\"nav-divider\"></li>\n");
		htmlBuilder.append("                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n");
		htmlBuilder.append("            	<span class=\"sr-only\">Toggle navigation</span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            </button>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n");
		htmlBuilder.append("            <ul class=\"nav navbar-nav navbar-right\">\n");
		htmlBuilder.append("                <li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n");
		htmlBuilder.append("                <li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n");
		htmlBuilder.append("                <li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n");
		htmlBuilder.append("                <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n");
		htmlBuilder.append("                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</div>\n");
		htmlBuilder.append("\n");
		htmlBuilder.append("<div class=\"container\" id=\"main\">\n");
		htmlBuilder.append("   <div class=\"col-md-10 col-md-offset-1\">\n");
		htmlBuilder.append("      <div class=\"panel panel-default\">\n");
		htmlBuilder.append("          <table class=\"table table-hover\">\n");
		htmlBuilder.append("              <thead>\n");
		htmlBuilder.append("                <tr>\n");
		htmlBuilder.append("                    <th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>\n");
		htmlBuilder.append("                </tr>\n");
		htmlBuilder.append("              </thead>\n");
		htmlBuilder.append("              <tbody>\n");

		StringBuilder userListBuilder = new StringBuilder();
		List<User> userList = UserDatabase.getUserList();

		AtomicInteger userCount = new AtomicInteger(1);
		userList.forEach(user -> {
			userListBuilder.append("                <tr>\n");
			userListBuilder.append("                    <th scope=\"row\">").append(userCount.get()).append("</th> <td>").append(user.getUserId()).append("</td> <td>").append(user.getName()).append("</td> <td>").append(user.getEmail()).append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
			userListBuilder.append("                </tr>\n");
			userCount.incrementAndGet();
		});

		htmlBuilder.append(userListBuilder);

		htmlBuilder.append("              </tbody>\n");
		htmlBuilder.append("          </table>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</div>\n");
		htmlBuilder.append("\n");
		htmlBuilder.append("<!-- script references -->\n");
		htmlBuilder.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>\n");
		htmlBuilder.append("<script src=\"../js/bootstrap.min.js\"></script>\n");
		htmlBuilder.append("<script src=\"../js/scripts.js\"></script>\n");
		htmlBuilder.append("</body>\n");
		htmlBuilder.append("</html>\n");
	}

	private static void buildLoginHtml(StringBuilder htmlBuilder) {
		htmlBuilder.append("<!DOCTYPE html>\n");
		htmlBuilder.append("<html lang=\"kr\">\n");
		htmlBuilder.append("<head>\n");
		htmlBuilder.append("    <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
		htmlBuilder.append("    <meta charset=\"utf-8\">\n");
		htmlBuilder.append("    <title>SLiPP Java Web Programming</title>\n");
		htmlBuilder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
		htmlBuilder.append("    <link href=\"../css/bootstrap.min.css\" rel=\"stylesheet\">\n");
		htmlBuilder.append("    <!--[if lt IE 9]>\n");
		htmlBuilder.append("    <script src=\"//html5shim.googlecode.com/svn/trunk/html5.js\"></script>\n");
		htmlBuilder.append("    <![endif]-->\n");
		htmlBuilder.append("    <link href=\"../css/styles.css\" rel=\"stylesheet\">\n");
		htmlBuilder.append("</head>\n");
		htmlBuilder.append("<body>\n");
		htmlBuilder.append("<nav class=\"navbar navbar-fixed-top header\">\n");
		htmlBuilder.append("    <div class=\"col-md-12\">\n");
		htmlBuilder.append("        <div class=\"navbar-header\">\n");
		htmlBuilder.append("            <a href=\"../index.html\" class=\"navbar-brand\">SLiPP</a>\n");
		htmlBuilder.append("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse1\">\n");
		htmlBuilder.append("                <i class=\"glyphicon glyphicon-search\"></i>\n");
		htmlBuilder.append("            </button>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse1\">\n");
		htmlBuilder.append("            <form class=\"navbar-form pull-left\">\n");
		htmlBuilder.append("                <div class=\"input-group\" style=\"max-width:470px;\">\n");
		htmlBuilder.append("                    <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"srch-term\" id=\"srch-term\">\n");
		htmlBuilder.append("                    <div class=\"input-group-btn\">\n");
		htmlBuilder.append("                        <button class=\"btn btn-default btn-primary\" type=\"submit\"><i class=\"glyphicon glyphicon-search\"></i></button>\n");
		htmlBuilder.append("                    </div>\n");
		htmlBuilder.append("                </div>\n");
		htmlBuilder.append("            </form>\n");
		htmlBuilder.append("            <ul class=\"nav navbar-nav navbar-right\">\n");
		htmlBuilder.append("                <li>\n");
		htmlBuilder.append("                    <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-bell\"></i></a>\n");
		htmlBuilder.append("                    <ul class=\"dropdown-menu\">\n");
		htmlBuilder.append("                        <li><a href=\"https://slipp.net\" target=\"_blank\">SLiPP</a></li>\n");
		htmlBuilder.append("                        <li><a href=\"https://facebook.com\" target=\"_blank\">Facebook</a></li>\n");
		htmlBuilder.append("                    </ul>\n");
		htmlBuilder.append("                </li>\n");
		htmlBuilder.append("                <li><a href=\"../user/list.html\"><i class=\"glyphicon glyphicon-user\"></i></a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</nav>\n");
		htmlBuilder.append("<div class=\"navbar navbar-default\" id=\"subnav\">\n");
		htmlBuilder.append("    <div class=\"col-md-12\">\n");
		htmlBuilder.append("        <div class=\"navbar-header\">\n");
		htmlBuilder.append("            <a href=\"#\" style=\"margin-left:15px;\" class=\"navbar-btn btn btn-default btn-plus dropdown-toggle\" data-toggle=\"dropdown\"><i class=\"glyphicon glyphicon-home\" style=\"color:#dd1111;\"></i> Home <small><i class=\"glyphicon glyphicon-chevron-down\"></i></small></a>\n");
		htmlBuilder.append("            <ul class=\"nav dropdown-menu\">\n");
		htmlBuilder.append("                <li><a href=\"../user/profile.html\"><i class=\"glyphicon glyphicon-user\" style=\"color:#1111dd;\"></i> Profile</a></li>\n");
		htmlBuilder.append("                <li class=\"nav-divider\"></li>\n");
		htmlBuilder.append("                <li><a href=\"#\"><i class=\"glyphicon glyphicon-cog\" style=\"color:#dd1111;\"></i> Settings</a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("            <button type=\"button\" class=\"navbar-toggle\" data-toggle=\"collapse\" data-target=\"#navbar-collapse2\">\n");
		htmlBuilder.append("            	<span class=\"sr-only\">Toggle navigation</span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            	<span class=\"icon-bar\"></span>\n");
		htmlBuilder.append("            </button>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("        <div class=\"collapse navbar-collapse\" id=\"navbar-collapse2\">\n");
		htmlBuilder.append("            <ul class=\"nav navbar-nav navbar-right\">\n");
		htmlBuilder.append("                <li class=\"active\"><a href=\"../index.html\">Posts</a></li>\n");
		htmlBuilder.append("                <li><a href=\"../user/login.html\" role=\"button\">로그인</a></li>\n");
		htmlBuilder.append("                <li><a href=\"../user/form.html\" role=\"button\">회원가입</a></li>\n");
		htmlBuilder.append("                <li><a href=\"#\" role=\"button\">로그아웃</a></li>\n");
		htmlBuilder.append("                <li><a href=\"#\" role=\"button\">개인정보수정</a></li>\n");
		htmlBuilder.append("            </ul>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</div>\n");
		htmlBuilder.append("\n");
		htmlBuilder.append("<div class=\"container\" id=\"main\">\n");
		htmlBuilder.append("   <div class=\"col-md-6 col-md-offset-3\">\n");
		htmlBuilder.append("      <div class=\"panel panel-default content-main\">\n");
		htmlBuilder.append("          <form name=\"question\" method=\"post\" action=\"/user/login\">\n");
		htmlBuilder.append("              <div class=\"form-group\">\n");
		htmlBuilder.append("                  <label for=\"userId\">사용자 아이디</label>\n");
		htmlBuilder.append("                  <input class=\"form-control\" id=\"userId\" name=\"userId\" placeholder=\"User ID\">\n");
		htmlBuilder.append("              </div>\n");
		htmlBuilder.append("              <div class=\"form-group\">\n");
		htmlBuilder.append("                  <label for=\"password\">비밀번호</label>\n");
		htmlBuilder.append("                  <input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" placeholder=\"Password\">\n");
		htmlBuilder.append("              </div>\n");
		htmlBuilder.append("              <button type=\"submit\" class=\"btn btn-success clearfix pull-right\">로그인</button>\n");
		htmlBuilder.append("              <div class=\"clearfix\" />\n");
		htmlBuilder.append("          </form>\n");
		htmlBuilder.append("        </div>\n");
		htmlBuilder.append("    </div>\n");
		htmlBuilder.append("</div>\n");
		htmlBuilder.append("\n");
		htmlBuilder.append("<!-- script references -->\n");
		htmlBuilder.append("<script src=\"../js/jquery-2.2.0.min.js\"></script>\n");
		htmlBuilder.append("<script src=\"../js/bootstrap.min.js\"></script>\n");
		htmlBuilder.append("<script src=\"../js/scripts.js\"></script>\n");
		htmlBuilder.append("</body>\n");
		htmlBuilder.append("</html>\n");
	}

	private static boolean isLoginUser(String sid) {
		if(Objects.nonNull(sid)) {
			Optional<String> loginUser = SessionStorage.getSessionUserId(sid);
			if(loginUser.isPresent()) {
				String userId = loginUser.get();
				Optional<User> userById = UserDatabase.findUserById(userId);
				if(userById.isPresent()) {
					return true;
				}
			}
		}

		return false;
	}
}
