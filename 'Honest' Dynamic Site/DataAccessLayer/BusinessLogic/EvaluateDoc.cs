// Glenn Findlay

using DataAccessLayer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;

namespace DataAccessLayer.BusinessLogic
{
    //represents a matching section of text
    public class Match
    {

        public int m_userStart;
        public int m_controlStart;
        public int m_length;
        public string m_match;

        public string m_frontPadUser;
        public string m_backPadUser;

        public string m_frontPadControl;
        public string m_backPadControl;

        public string m_userDocName;
        public string m_controlDocName;

        public string m_controlDocAuthor;
    }

    public static class EvaluateDoc
    {

        public static List<Match> findMatches(string user, string control, string controlName, string userName, string controlAuthor)
        {

            int windowSize = 15;        //matches under this length won't be measured
            int matchFloor = 30;        // matches under this length will be discarded
            int padSize = 35;

            List<Match> matches = new List<Match>();

            // remove newlines
            user = user.Replace("\n", " ").Replace("\r", " ");
            control = control.Replace("\n", " ").Replace("\r", " ");

            // remove double spaces
            user = user.Replace("  ", " ");
            control = control.Replace("  ", " ");

            //find matching strings
            for (int i = 0; i + windowSize < user.Length; i++)
            {
                string substring = user.Substring(i, windowSize);
                int x = control.IndexOf(substring);
                if (x != -1)
                {
                    int userStart = i;
                    int controlStart = x;
                    int currentMatchLen = 1;
                    string currentMatch = user.Substring(i, 1);
                    i++;
                    x++;

                    // find matches
                    while (i < user.Length && x < control.Length && control.Substring(x, 1).Equals(user.Substring(i, 1)))
                    {
                        currentMatchLen++;
                        currentMatch += user.Substring(i, 1);
                        i++;
                        x++;
                    }

                    // user pad
                    int userPadStartF = userStart - padSize;
                    if (userPadStartF < 0) userPadStartF = 0;

                    int userPadStartB = userStart + currentMatchLen;
                    int userPadEndB = userPadStartB + padSize;
                    if(userPadEndB > user.Length - 1) userPadEndB = user.Length - 1;


                    //control pad
                    int controlPadStartF = controlStart - padSize;
                    if(controlPadStartF < 0) controlPadStartF = 0;

                    int controlPadStartB = controlStart + currentMatchLen;
                    int controlPadEndB = controlPadStartB + padSize;
                    if(userPadEndB > user.Length - 1) userPadEndB = user.Length-1;

                    // pad strings
                    string fpu = "";
                    if (userStart > 0) fpu += "...";
                    fpu += user.Substring(userPadStartF, userStart - userPadStartF);

                    string bpu = "";
                    if (userPadEndB <= userPadStartB) bpu = ""; 
                    else bpu = user.Substring(userPadStartB, userPadEndB - userPadStartB) + "...";

                    string fpc = "";
                    if (controlStart > 0) fpc += "...";
                    fpc += control.Substring(controlPadStartF, controlStart - controlPadStartF);

                    string bpc = "";
                    if (controlPadEndB <= controlPadStartB) bpc = "";
                    else bpc = control.Substring(controlPadStartB, controlPadEndB - controlPadStartB) + "...";
                    
                    char n = currentMatch[0];
                    for (int j = 0; j < currentMatch.Length && currentMatch[j].Equals(' '); j++)
                    {
                        currentMatch = currentMatch.Substring(j + 1, currentMatch.Length - 1);
                    }

                    int m = currentMatch.Length - 1;
                     n = currentMatch[m];
                    for (int j = currentMatch.Length - 1; j > 0 && currentMatch[j].Equals(' '); j--)
                    {
                        currentMatch = currentMatch.Substring(0, j);
                    }
                                    
                    Match match = new Match()
                    {
                        m_userStart = userStart,
                        m_controlStart = controlStart,
                        m_length = currentMatchLen,
                        m_match = currentMatch,

                        m_frontPadUser = fpu,
                        m_backPadUser = bpu,
                        m_frontPadControl = fpc,
                        m_backPadControl = bpc,

                        m_userDocName = userName,
                       m_controlDocName = controlName,

                       m_controlDocAuthor = controlAuthor,

                    };
                    matches.Add(match);

                }


            }

            matches.RemoveAll(x => x.m_length < matchFloor);
            return matches;
        }

        //Find match resulsts for a given document
        public static List<Match> evalDocMatches(string docText, string docName)
        {

            List<Match> results = new List<Match>();
            List<ControlDocDAL> controlDocs = ControlDocProcessor.loadControlDocs();

            //find matches
            foreach (ControlDocDAL cd in controlDocs)
            {
                  List<Match> matches = findMatches(docText, cd.ControlDocText, cd.ControlDocName, docName, cd.ControlDocAuthor);
                
                foreach(Match m in matches)
                {
                    results.Add(m);
                }

            }

                return results;
        }


        }
}
