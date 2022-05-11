// Glenn Findlay

using DataAccessLayer.DataAccess;
using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.BusinessLogic
{
    public static class ControlDocProcessor
    {

        public static List<ControlDocDAL> loadControlDocs()
        {
            string sql = @"select ControlDocID, ControlDocName, ControlDocAuthor, ControlDocText
                            from dbo.ControlDoc";


            return SQLDataAccess.LoadData<ControlDocDAL>(sql);
        }



    }
}
