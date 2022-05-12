//Glenn Findlay

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//Class represents an entire fuse panel
public class FuseControl : MonoBehaviour
{

    [SerializeField] private List<FuseSlot> fuses = new List<FuseSlot>();

    private bool m_activated = false;

    [SerializeField] private GameObject offLight1;
    [SerializeField] private GameObject offLight2;

    [SerializeField] private GameObject onLight1;
    [SerializeField] private GameObject onLight2;

    // Update is called once per frame
    void Update()
    {
        //deactivate if fuses missing
        if(fuses.FindAll(x => x.powered == false).Count > 0) { 
            if(m_activated) deActivate();
        }
        else if(!m_activated) activate();

    }

    void activate()
    {
        offLight1.SetActive(false);
        offLight2.SetActive(false);
        onLight1.SetActive(true);
        onLight2.SetActive(true);
    }

    void deActivate()
    {
        offLight1.SetActive(true);
        offLight2.SetActive(true);
        onLight1.SetActive(false);
        onLight2.SetActive(false);
    }

}
