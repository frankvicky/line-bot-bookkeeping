package bookkeeping.application.controller;

import bookkeeping.application.entity.Bill;
import bookkeeping.application.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@Slf4j
public class LiffController {

    private BillService billService;

    @Value("${pageid}")
    private String pageid;


    private Set<String> hash;

    public LiffController(BillService billService) {
        this.billService = billService;
        hash = new HashSet<>();

    }

    @GetMapping("/demo")
    public String getIndexHTML() {
        return "index";
    }

    @GetMapping("/personalAccount")
    public String getAccount(Model model) {
        String sessionKey = UUID.randomUUID().toString();
        hash.add(sessionKey);
        model.addAttribute("requestId", sessionKey);
        model.addAttribute("pageid", pageid);
        return "personalAccount";
    }

    @GetMapping("/query")
    @ResponseBody
    public String test(@RequestParam String UID, @RequestParam String session) {
        log.warn(UID);
        List<Bill> billList = new ArrayList<>();
        if(hash.contains(session)) {
            billList = billService.findByFK(UID);
            log.warn(billList.get(0).getItem());
            log.warn(billList.get(0).toString());
            log.warn(String.valueOf(billList.size()));
            hash.remove(session);

        }
        return billList.get(0).toString();
    }
}
