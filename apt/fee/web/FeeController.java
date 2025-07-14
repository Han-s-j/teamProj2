package com.project.apt.fee.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.apt.fee.dao.IFeeDAO;
import com.project.apt.fee.service.FeeService;
import com.project.apt.fee.vo.FeeAptAddressVO;
import com.project.apt.fee.vo.FeeCompareVO;
import com.project.apt.fee.vo.FeeElecVO;
import com.project.apt.fee.vo.FeeVO;
import com.project.apt.user.vo.UserVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class FeeController {
	
	@Autowired
	private IFeeDAO feeDAO;
	
	@Autowired
	FeeService service;

	@PostMapping("/confirm")
	public String confirmPost(FeeVO fee, Model model, HttpSession session) {
		// 이곳에서 DB 저장 혹은 세션/모델 처리 등을 수행
		UserVO user = (UserVO) session.getAttribute("loginUser");
		fee.setUserId(user.getUserId());
		
		// 저장 처리해야함!
		feeDAO.insertFee(fee);
		System.out.println("✅ 저장 완료: " + fee);
		
		System.out.println(fee);

		return "redirect:/feeFeedBack";
		
	}
	@GetMapping("/feeFeedBack")
	public String showFeeChart(HttpSession session, Model model) {
	    UserVO user = (UserVO) session.getAttribute("loginUser");
	    
	    if (user == null) {
	        // 예: 로그인 페이지로 리다이렉트
	        return "redirect:/login"; 
	    }
	    
	    String userId = user.getUserId();
	    
	    List<FeeCompareVO> chartData = feeDAO.selectUserAndAvgFees(userId);
	    
	    if (chartData == null) {
	        chartData = new ArrayList<>(); // null인 경우 빈 리스트로 초기화
	    }
	    System.out.println(chartData);
	    model.addAttribute("chartData", chartData);
	    return "fee/feeFeedBack";  // chart를 보여줄 HTML 파일
	}
	
	@GetMapping("/feeCompare") // 관리비 비교 페이지
	public String feeCompareMove(Model model) {
	    List<String> districtList = service.getDistrictList();
	    List<String> yearList = service.getYearList();

	    model.addAttribute("districtList", districtList);
	    model.addAttribute("yearList", yearList);

	    return "fee/feeCompare";
	}
	
	@GetMapping("/getMonthList")
	@ResponseBody
	public List<String> getMonthList(@RequestParam int year) {
	    return service.getMonthList(year);
	}
	
	@GetMapping("/getDongList")
	@ResponseBody
	public List<String> getDongList(@RequestParam String district){
		return service.getDongList(district);
	}
	
	@GetMapping("/getAptList")
	@ResponseBody
	public List<FeeAptAddressVO> getAptList(@RequestParam String dong) {
	    return service.getAptList(dong);
	}
	
	@GetMapping("/feeComparePopup") // 비교 팝업
	public String feeComparePopupMove() {
		return "fee/feeComparePopup";
	}
	
	@GetMapping("/getElecFee")
	@ResponseBody
	public Map<String, FeeElecVO> getElectricFeeDetail(@RequestParam String myKaptCode,
	                                                   @RequestParam String otherKaptCode,
	                                                   @RequestParam int year,
	                                                   @RequestParam int month) {
	    Map<String, FeeElecVO> result = new HashMap<>();
	    result.put("my", service.getElecFee(myKaptCode, year, month));
	    result.put("other", service.getElecFee(otherKaptCode, year, month));
	    return result;
	}
	
	@GetMapping("/getMgmtFee")
	@ResponseBody
	public Map<String, Integer> getMgmtFee(@RequestParam String myKaptCode,
	                                       @RequestParam String otherKaptCode,
	                                       @RequestParam int year,
	                                       @RequestParam int month) {
	    Map<String, Integer> result = new HashMap<>();
	    result.put("myCost", service.getMgmtFee(myKaptCode, year, month));
	    result.put("otherCost", service.getMgmtFee(otherKaptCode, year, month));
	    return result;
	}

}
