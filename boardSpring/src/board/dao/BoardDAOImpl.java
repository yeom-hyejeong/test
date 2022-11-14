package board.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import board.dto.BoardDTO;

public class BoardDAOImpl implements BoardDAO {
	private JdbcTemplate jdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	MyRowMapper mapper = new MyRowMapper();

	class MyRowMapper implements RowMapper<BoardDTO>{
		@Override
		public BoardDTO mapRow(ResultSet rs, int count) throws SQLException {
			BoardDTO dto = new BoardDTO();
			dto.setNum(rs.getInt("num"));
			dto.setWriter(rs.getString("writer"));
			dto.setEmail(rs.getString("email"));
			dto.setSubject(rs.getString("subject"));
			dto.setPasswd(rs.getString("passwd"));
			dto.setReg_date(rs.getString("reg_date"));
			dto.setReadcount(rs.getInt("readcount"));
			dto.setContent(rs.getString("content"));
			dto.setIp(rs.getString("ip"));
			dto.setRe_step(rs.getInt("re_step"));
			dto.setRe_level(rs.getInt("re_level"));
			return dto;
		}
	}
	
	@Override
	public List<BoardDTO> listBoard(int start, int end) {
		String sql  = "select * from (select rownum rn, A.* from "
				+ "(select * from board order by re_step asc)A) "
				+ "where rn between ? and ?";
		Object[] values = new Object[] {start, end};
		List<BoardDTO> list = jdbcTemplate.query(sql, values, mapper);
		return list;
	}

	@Override
	public BoardDTO getBoard(int num, String mode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertBoard(BoardDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteBoard(int num, String passwd) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateBoard(BoardDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCount() {
		String sql = "select count(*) from board";
		int count = jdbcTemplate.queryForInt(sql);
		return count;
	}

}






