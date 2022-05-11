// Glenn Findlay

using DataAccessLayer.DataAccess;
using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.BusinessLogic
{
    public static class UserDocProcessor
    {


        public static int CreateUserDoc(int docID, string docName, string docText, string session)
        {
            UserDocDAL data = new UserDocDAL
            {
            UserDocID = docID,
            UserDocName = docName,
            UserDocText = docText,
            SessionID = session
        };

            //first set of variables is DB side, second are within the object you are converting           
            string sql = @"insert into dbo.UserDoc (UserDocID, UserDocName, UserDocText, SessionID)
                        values (@UserDocID, @UserDocName, @UserDocText, @SessionID);";

            return SQLDataAccess.SaveData(sql, data);
        }


        public static int DeleteUserDoc(int docID)
        {

            string sql = @"DELETE dbo.UserDoc WHERE UserDocID = @docID";

            SqlCommand command  = new SqlCommand();
            command.CommandText = sql;
            command.Parameters.Add("@docID", SqlDbType.Int).Value = docID;


            return SQLDataAccess.RemoveData(command);
        }



        public static UserDocDAL getUserDoc(int docID)
        {

            string sql = @"select UserDocID, UserDocName, UserDocText, SessionID
                            from dbo.UserDoc WHERE UserDocID = @docID;";

            return SQLDataAccess.LoadRow(sql, docID);
        }




        public static List<UserDocDAL> loadUserDocs(string session)
        {
            string sql = @"select UserDocID, UserDocName, UserDocText, SessionID
                            from dbo.UserDoc WHERE SessionID = @session;";


            return SQLDataAccess.LoadData<UserDocDAL>(sql, session);
        }


    }
}
