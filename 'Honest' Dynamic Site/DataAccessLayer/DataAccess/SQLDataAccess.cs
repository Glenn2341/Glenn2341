// Glenn Findlay

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Dapper;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using DataAccessLayer.Models;
using Microsoft.Extensions.Configuration;

namespace DataAccessLayer.DataAccess
{
    public static class SQLDataAccess
    {
       
        //retrieve connection string from json
        public static string GetConnectionString()
        {


            String c = new ConfigurationBuilder().AddJsonFile("appsettings.json").Build().GetSection("ConnectionStrings")["DefaultConnection"];



            return c;
        }


        public static List<T> LoadData<T>(string sql)
        {
            using (IDbConnection cnn = new SqlConnection(GetConnectionString()))
            {
                return cnn.Query<T>(sql).ToList();
            }
        }

        public static List<T> LoadData<T>(string sql, string session)
        {
            using (IDbConnection cnn = new SqlConnection(GetConnectionString()))
            {
                return cnn.Query<T>(sql, new {@session=session}).ToList();
            }
        }


        public static UserDocDAL LoadRow(string sql, int ID)
        {
            using (IDbConnection cnn = new SqlConnection(GetConnectionString()))
            {
                return cnn.Query<UserDocDAL>(sql, new { @docID = ID }).First();
            }
        }


        //enter into database, returns number of rows affected
        public static int SaveData<T>(string sql, T data)
        {
            using (IDbConnection cnn = new SqlConnection(GetConnectionString()))
            {
                return cnn.Execute(sql, data);
            }
        }

        //remove data, non-dapper way
        public static int RemoveData(SqlCommand sql)
        {
            using (SqlConnection cnn = new SqlConnection(GetConnectionString()))
            {
                cnn.Open();
                sql.Connection = (SqlConnection)cnn;
               return sql.ExecuteNonQuery();
            }
        }


    }
}
