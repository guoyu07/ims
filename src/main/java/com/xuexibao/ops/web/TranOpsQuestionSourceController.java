package com.xuexibao.ops.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.enumeration.TranOpsAuditStatus;
import com.xuexibao.ops.model.TranOps;
import com.xuexibao.ops.service.OrcPictureService;
import com.xuexibao.ops.service.TikuTeamService;
import com.xuexibao.ops.service.TranOpsService;


@Controller
@RequestMapping(value = "/tranops")
public class TranOpsQuestionSourceController extends AbstractController {
	
	@Autowired
	protected TranOpsService tranOpsService;
	@Autowired
	protected TikuTeamService tikuTeamService;
	
	@Autowired
	protected OrcPictureService orcPictureService;
	
	@RequestMapping(value = "/orgQuestionByOps")
	public String orgQuestionByOps(ModelMap model, Long questionId) {
		TranOps tranOps = tranOpsService.getTranOpsById(questionId);
    	if(tranOps != null) {
    		model.addAttribute("tranOps", tranOps);
			if(TranOpsAuditStatus.AUDIT_THROUGH.getId().equals(tranOps.getAuditStatus())) {
				return "tranops/tranOpsEditViewDetail";
			} else {
				return "tranops/tranOpsEditDetail";
			}
    	}
    	return null;
	}		
	
}
