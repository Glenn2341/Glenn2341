// Glenn Findlay

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccessLayer.Models
{
    public class UserDocDAL
    {
        public int tableRowID { get; set; } //ID of the row in the database table
        public int UserDocID { get; set; }
        public string UserDocName { get; set; }
        public string UserDocText { get; set; }
        public string SessionID { get; set; }

    }
}
