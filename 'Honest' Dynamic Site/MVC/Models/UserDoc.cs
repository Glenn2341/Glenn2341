using Microsoft.AspNetCore.Http;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Honest.Models
{
    public class UserDoc
    {
        
        


        [Key]
        public int userDocID { get; set; }

        public string userDocName { get; set; }

        public string userDocText { get; set; }




    }
}
