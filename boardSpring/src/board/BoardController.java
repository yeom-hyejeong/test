package board;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import board.dao.BoardDAO;
import board.dto.BoardDTO;
import board.mybatis.BoardMapper;

@Controller
public class BoardController {
	
	@Autowired
	private BoardDAO boardDAO;
	
	@RequestMapping(value="/list_board.do")
	public String listBoard(HttpServletRequest req, @RequestParam(required = false) String pageNum) {
		if (pageNum == null) {
			pageNum = "1";
		}
		int currentPage = Integer.parseInt(pageNum);
		int pageSize = 5;
		int startRow = (currentPage-1) * pageSize + 1;
		int endRow = startRow + pageSize - 1;
		//int countRow = boardDAO.getCount();
		int countRow = BoardMapper.getCount();
		if (endRow > countRow) endRow = countRow;
		
		//List<BoardDTO> list = boardDAO.listBoard(startRow, endRow);
		List<BoardDTO> list = BoardMapper.listBoard(startRow, endRow);
		int num = countRow - (startRow - 1);
		req.setAttribute("listBoard", list);
		req.setAttribute("num", num);
		int pageCount = countRow / pageSize + (countRow%pageSize==0 ? 0 : 1);
		int pageBlock = 3;
		int startPage = (currentPage - 1) / pageBlock * pageBlock + 1;
		int endPage = startPage + pageBlock - 1;
		if (endPage > pageCount) endPage = pageCount;		
		req.setAttribute("pageCount", pageCount);
		req.setAttribute("pageBlock", pageBlock);
		req.setAttribute("startPage", startPage);
		req.setAttribute("endPage", endPage);
		return "/list";
	}
	
	@RequestMapping(value="/write_board.do", method=RequestMethod.GET)
	public String writeForm_board() {
		return "/writeForm";
	}
	
	@RequestMapping(value="/write_board.do", method=RequestMethod.POST)
	public String writePro_board(HttpServletRequest req, 
			@ModelAttribute BoardDTO dto, BindingResult result) {
		if (result.hasErrors()) {
			dto.setNum(0);
			dto.setRe_step(0);
			dto.setRe_level(0);
		}
		dto.setIp(req.getRemoteAddr());
		//int res = boardDAO.insertBoard(dto);
		int res = BoardMapper.insertBoard(dto);
		if (res>0) {
			req.setAttribute("msg", "게시글 등록 성공!! 게시글 목록 페이지로 이동합니다.");
			req.setAttribute("url", "list_board.do");
		}else {
			req.setAttribute("msg", "게시글 등록 실패!! 게시글 등록 페이지로 이동합니다.");
			req.setAttribute("url", "write_board.do");
		}
		return "forward:message.jsp";
	}
	
	@RequestMapping(value="/content_board.do")
	public String content_board(HttpServletRequest req, @RequestParam int num){
		//BoardDTO dto = boardDAO.getBoard(num, "content");
		BoardDTO dto = BoardMapper.getBoard(num, "content");
		req.setAttribute("getBoard", dto);
		return "/content";
	}
	
	@RequestMapping(value="/delete_board.do", method=RequestMethod.GET)
	public String deleteForm_board() {
		return "/deleteForm";
	}
	
	@RequestMapping(value="/delete_board.do", method=RequestMethod.POST)
	public String deletePro_board(HttpServletRequest req, 
										@RequestParam Map<String, String> params) {
		//int res = boardDAO.deleteBoard(Integer.parseInt(params.get("num")), params.get("passwd"));
		int res = BoardMapper.deleteBoard(params);
		if (res>0) {
			req.setAttribute("msg", "게시글 삭제 성공!! 게시글 목록 페이지로 이동합니다.");
			req.setAttribute("url", "list_board.do");
		}else if (res==0){
			req.setAttribute("msg", "게시글 삭제 실패!! 게시글 상세보기 페이지로 이동합니다.");
			req.setAttribute("url", "content_board.do?num="+params.get("num"));
		}else{
			req.setAttribute("msg", "비밀번호가 틀렸습니다. 다시 입력해 주세요!!");
			req.setAttribute("url", "delete_board.do?num="+params.get("num"));
		}
		
		return "forward:message.jsp";
	}
	
	@RequestMapping(value="/update_board.do", method=RequestMethod.GET)
	public String updateForm_board(HttpServletRequest req, @RequestParam int num) {
		//BoardDTO dto = boardDAO.getBoard(num, "update");
		BoardDTO dto = BoardMapper.getBoard(num, "update");
		req.setAttribute("getBoard", dto);
		return "/updateForm";
	}
	
	@RequestMapping(value="/update_board.do", method=RequestMethod.POST)
	public String updatePro_board(HttpServletRequest req, 
							@ModelAttribute BoardDTO dto, BindingResult result) {
		//int res = boardDAO.updateBoard(dto);
		int res = BoardMapper.updateBoard(dto);
		if (res>0) {
			req.setAttribute("msg", "게시글 수정 성공!! 게시글 목록 페이지로 이동합니다.");
			req.setAttribute("url", "list_board.do");
		}else if (res==0){
			req.setAttribute("msg", "게시글 수정 실패!! 게시글 상세보기 페이지로 이동합니다.");
			req.setAttribute("url", "content_board.do?num="+dto.getNum());
		}else{
			req.setAttribute("msg", "비밀번호가 틀렸습니다. 다시 입력해 주세요!!");
			req.setAttribute("url", "update_board.do?num="+dto.getNum());
		}
		return "forward:message.jsp";
	}
}


