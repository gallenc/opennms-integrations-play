package org.opennms.poc.api.roadfaultapi.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opennms.poc.api.roadfaultapi.dao.RequestResponseLogRepository;
import org.opennms.poc.api.roadfaultapi.model.ErrorMessage;
import org.opennms.poc.api.roadfaultapi.model.RequestResponseLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MVCController {

    private static final Logger LOG = LogManager.getLogger(MVCController.class);

    static final int DEFAULT_ITEMS_PER_PAGE = 10;

    @Autowired
    private MessageLoggerService messageLoggerService;

    @Autowired
    private RequestResponseLogRepository requestResponseLogRepository;

    // this redirects calls to the root of our application to index.html
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/home", method = {RequestMethod.GET, RequestMethod.POST})
    public String viewMessages(@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "password", required = false) String password,
            @RequestParam(name = "error_message", required = false) String error_message,
            @RequestParam(name = "error_type", required = false) String error_type,
            Model model
    ) {

        // used to set tab selected
        model.addAttribute("selectedPage", "home");

        String message = "";
        String errorMessage = "";

        if (action == null) {
            // do nothing but show page
        } else if ("replyWithErrorMessage".equals(action)) {
            ErrorMessage em = new ErrorMessage();
            em.setError_message(error_message);
            em.setError_type(error_type);
            messageLoggerService.setTestErrorMessage(em);
            message = "reply with error message set";
        } else if ("clearErrorMessage".equals(action)) {
            messageLoggerService.setTestErrorMessage(null);
            message = "no reply with error message";
        } else if ("changeCredentials".equals(action)) {
            messageLoggerService.setPassword(password);
            messageLoggerService.setUsername(username);
            message = "credentials changed username=" + username
                    + " password=" + password;
        }

        username = messageLoggerService.getUsername();
        model.addAttribute("username", username);
        password = messageLoggerService.getPassword();
        model.addAttribute("password", password);

        ErrorMessage em = messageLoggerService.getTestErrorMessage();
        if (em != null) {
            error_message = em.getError_message();
            error_type = em.getError_type();
        }
        model.addAttribute("error_message", error_message);
        model.addAttribute("error_type", error_type);

        model.addAttribute("message", message);

        return "home";
    }

    @RequestMapping(value = "/log", method = {RequestMethod.GET, RequestMethod.POST})
    public String log(@RequestParam(name = "action", required = false) String action,
            @RequestParam(name = "pageno", required = false) String pageno,
            @RequestParam(name = "itemsPerPage", required = false) String itemsPerPageStr,
            @RequestParam(name = "currentPage", required = false) String currentPageStr,
            Model model) {
        // used to set tab selected
        model.addAttribute("selectedPage", "log");

        String message = "";
        String errorMessage = "";

        if (action == null) {
            // do nothing but show page
        } else if ("resetLog".equals(action)) {
            message = "all log entries deleted";
            requestResponseLogRepository.deleteAll();
        }

        int itemsPerPage = (itemsPerPageStr == null || itemsPerPageStr.isBlank()) ? DEFAULT_ITEMS_PER_PAGE : Integer.parseInt(itemsPerPageStr);
        int pageNumber = (currentPageStr == null || currentPageStr.isBlank()) ? 0 : Integer.parseInt(currentPageStr);

        Page<RequestResponseLog> page = requestResponseLogRepository.findAll(PageRequest.of(pageNumber, itemsPerPage, Sort.by(Sort.Direction.DESC, "id")));

        pageNumber = page.getNumber(); // current page number
        int totalPages = page.getTotalPages();
        long totalElements = page.getTotalElements();
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("itemsPerPage", itemsPerPage);
        model.addAttribute("totalElements", totalElements);

        List<RequestResponseLog> requestLog = page.getContent();

        //List<RequestResponseLog> requestLog = requestResponseLogRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("requestLog", requestLog);

        model.addAttribute("message", message);

        return "log";
    }

    @RequestMapping(value = "/about", method = {RequestMethod.GET, RequestMethod.POST})
    public String about(Model model) {
        // used to set tab selected
        model.addAttribute("selectedPage", "about");
        return "about";
    }

    @RequestMapping(value = "/contact", method = {RequestMethod.GET, RequestMethod.POST})
    public String contact(Model model) {
        // used to set tab selected
        model.addAttribute("selectedPage", "contact");
        return "contact";
    }


    /*
     * Default exception handler, catches all exceptions, redirects to friendly
     * error page. Does not catch request mapping errors
     */
    @ExceptionHandler(Exception.class)
    public String myExceptionHandler(final Exception e, Model model, HttpServletRequest request) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        final String strStackTrace = sw.toString(); // stack trace as a string
        String urlStr = "not defined";
        if (request != null) {
            StringBuffer url = request.getRequestURL();
            urlStr = url.toString();
        }
        model.addAttribute("requestUrl", urlStr);
        model.addAttribute("strStackTrace", strStackTrace);
        model.addAttribute("exception", e);
        //logger.error(strStackTrace); // send to logger first
        return "error"; // default friendly exception message for user
    }

}
