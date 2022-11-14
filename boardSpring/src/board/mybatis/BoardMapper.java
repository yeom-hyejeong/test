package board.mybatis;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import board.dto.BoardDTO;

public class BoardMapper {
	private static SqlSessionFactory sqlMapper;
	static {
		try {
			String resource = "configuration.xml";
			Reader reader = Resources.getResourceAsReader(resource);
			sqlMapper = new SqlSessionFactoryBuilder().build(reader);
		}catch(IOException e) {
			throw new RuntimeException("DB 연결 오류 발생!!" + e.getMessage());
		}
	}
	
	public static List<BoardDTO> listBoard(int startRow, int endRow){
		SqlSession session = sqlMapper.openSession();
		try {
			Map<String, Integer> map = new Hashtable<>();
			map.put("start", startRow);
			map.put("end", endRow);
			List<BoardDTO> list = session.selectList("listBoard", map);
			return list;
		}finally {
			session.close();
		}
	}
	
	public static int insertBoard(BoardDTO dto) {
		SqlSession session = sqlMapper.openSession();
		try {
			String sql = null;
			if (dto.getNum() == 0) {
				sql = "update board set re_step = re_step + 1";
			}else {
				sql = "update board set re_step = re_step + 1 where re_step > "+dto.getRe_step();
				dto.setRe_step(dto.getRe_step()+1);
				dto.setRe_level(dto.getRe_level()+1);
			}
			Map<String, String> map = new Hashtable<>();
			map.put("sql", sql);
			session.update("plusRe_step", map);
			int res = session.insert("insertBoard", dto);
			session.commit();
			return res;
		}finally {
			session.close();
		}
	}
	
	public static BoardDTO getBoard(int num, String mode) {
		SqlSession session = sqlMapper.openSession();
		try {
			if (mode.equals("content")) {
				session.update("plusReadcount", num);
				session.commit();
			}
			BoardDTO dto = session.selectOne("getBoard", num);
			return dto;
		}finally {
			session.close();
		}
	}
	
	public static int deleteBoard(Map<String, String> map) {
		BoardDTO dto = getBoard(Integer.parseInt(map.get("num")), "password");
		if (dto.getPasswd().equals(map.get("passwd"))) {
			SqlSession session = sqlMapper.openSession();
			try {
				int res = session.delete("deleteBoard", Integer.parseInt(map.get("num")));
				session.commit();
				return res;
			}finally {
				session.close();
			}
		}else {
			return -1;
		}
	}
	
	public static int updateBoard(BoardDTO dto) {
		BoardDTO dto2 = getBoard(dto.getNum(), "password");
		if (dto.getPasswd().equals(dto2.getPasswd())) {
			SqlSession session = sqlMapper.openSession();
			try {
				int res = session.update("updateBoard", dto);
				session.commit();
				return res;
			}finally {
				session.close();
			}
		}else {
			return -1;
		}
	}
	
	public static int getCount() {
		SqlSession session = sqlMapper.openSession();
		try {
			int res = session.selectOne("getCount");
			return res;
		}finally {
			session.close();
		}
	}
}
