// Glenn Findlay

using Honest.Models;
using Microsoft.AspNetCore.Mvc;
using System.Diagnostics;
using DataAccessLayer.BusinessLogic;
using System.Text;
using Microsoft.AspNetCore.Hosting;
using System.Reflection;
using System.Configuration;
using System.Collections;
using iText.Kernel.Pdf;
using iText.Kernel.Pdf.Canvas.Parser;
using iText.Kernel.Pdf.Canvas.Parser.Listener;
using Spire.Doc;
using System.IO;

namespace Honest.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;
        

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
           
         }

  
        // Handle results request by evaluating document and forwarding to results view
            public ActionResult Results(UserDoc userDoc)
        {

            System.Diagnostics.Debug.WriteLine("showing resulsts");

            if (userDoc.userDocName == null)
            {
                userDoc.userDocName = "user-entered text";
            }

            List<Match> results = EvaluateDoc.evalDocMatches(userDoc.userDocText, userDoc.userDocName);


            ViewBag.evalResults = results;

            return View("Results");
        }

       
        // Handle upload action for .txt, pdf and .docx formats
        public ActionResult FileUpload(IFormFile file)
        {

            StringBuilder fileText = new StringBuilder();

            //process DOCX
            if (file.ContentType.Equals("application/octet-stream"))
            {

                Document doc = new Document();
                doc.LoadFromStream(file.OpenReadStream(), FileFormat.Docx);
           
                // spire.doc inserts an evaluation notice at the start
                fileText = new StringBuilder(doc.GetText().Substring(70));

            }


            // process PDF
            else if (file.ContentType.Equals("application/pdf"))
            {

              
                PdfReader PDFReader = new PdfReader(file.OpenReadStream());
                PdfDocument PDFDoc = new PdfDocument(PDFReader);
                ITextExtractionStrategy strat = new SimpleTextExtractionStrategy();

                for (int i = 1; i <= PDFDoc.GetNumberOfPages(); i++)
                {
                    string next = PdfTextExtractor.GetTextFromPage(PDFDoc.GetPage(i), strat);
                    fileText.AppendLine(next);
                }
            }

 
            // process txt
            else if (file.ContentType.Equals("text/plain"))
            {
                StreamReader reader = new StreamReader(file.OpenReadStream());
                while (reader.Peek() >= 0)
                    fileText.AppendLine(reader.ReadLine());
            }
            else
            {
                return View("Index");
            }
            

            string fileString = fileText.ToString();
            string fileName = file.FileName;

           //  make userdoc object
            UserDoc userDoc = new UserDoc
            {
                userDocID = new string(fileName + DateTime.Now.ToString()).GetHashCode(),
                userDocName = fileName,
                userDocText = fileString,
           };


            return Results(userDoc);
        }

        // "Index" main page
        public IActionResult Index()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }

       

    }
}