package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from user";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        new BigInteger(String.valueOf(rs.getInt("userId"))),
                        rs.getString("pass"),
                        rs.getString("name"),
                        rs.getString("email"))
                );
    }


   public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from user where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        new BigInteger(String.valueOf(rs.getInt("userId"))),
                        rs.getString("pass"),
                        rs.getString("name"),
                        rs.getString("email")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(BigInteger userId){
        String getUserQuery = "select * from user where userId = ?";
        BigInteger getUserParams = userId;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        new BigInteger(String.valueOf(rs.getInt("userId"))),
                        rs.getString("pass"),
                        rs.getString("name"),
                        rs.getString("email")),
                getUserParams);
    }


    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (pass, name, nickName, phoneNumber, myForgn, email) VALUES (?,?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getPass(), postUserReq.getName()
                , postUserReq.getNickName(), postUserReq.getPhoneNumber()
                , MyForgn.Local,postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }
/*
    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update user set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from user where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }

*/
}
