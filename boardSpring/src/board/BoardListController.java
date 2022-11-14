package board;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import board.dao.BoardDAO;
import board.dto.BoardDTO;

public class BoardListController implements Controller {

private BoardDAO boardDAO;
	
	public void setBoardDAO(BoardDAO boardDAO) {
		this.boardDAO = boardDAO;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		int pageSize = 5;
		String pageNum = req.getParameter("pageNum");
		if (pageNum == null){
			pageNum = "1";
		}
		int currentPage = Integer.parseInt(pageNum);
		int startRow = (currentPage-1) * pageSize + 1;
		int endRow = startRow + pageSize - 1;
		int countRow = boardDAO.getCount();
		if (endRow > countRow) endRow = countRow;
		
		List<BoardDTO> list = boardDAO.listBoard(startRow, endRow);
		int num = countRow - (startRow - 1);
		ModelAndView mav = new ModelAndView("/list");
		mav.addObject("listBoard", list);
		mav.addObject("num", num);
		return mav;
	}

}
