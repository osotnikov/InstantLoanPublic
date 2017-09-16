package osotnikov.demo.instantloan.web;
 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
 
@Controller
public class WelcomeController {
 
	@RequestMapping("/")
	public ModelAndView welcome() {
		System.out.println("in LoanInterestCalculatorController");
 
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}
}