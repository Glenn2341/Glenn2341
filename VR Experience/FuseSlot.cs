//Glenn Findlay

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//This class represents a slot for a fuse on the panel, which may be filled or empty
public class FuseSlot : MonoBehaviour
{

    [SerializeField] private Transform fuseSlotLocation;
    public bool powered { get; private set; } = false;
    [SerializeField] private GameObject myFuse;
    [SerializeField] private int m_fusetype = 0;

    //FixedUpdate is called once per physics update (not tied to framerate)
    void FixedUpdate()
    {
        //check for a valid fuse
        if (myFuse != null && myFuse.GetComponent<Fuse>().isBurned() == false && myFuse.GetComponent<Fuse>().getfuseType() == m_fusetype)
        {
            powered = true;
        }
        else powered = false;

        //update physics on the slotted fuse
        if(myFuse != null && !myFuse.GetComponent<OVRGrabbable>().isGrabbed)
        {
            myFuse.GetComponent<Rigidbody>().isKinematic = true;
            myFuse.transform.parent = this.transform;
            myFuse.transform.position = fuseSlotLocation.position;
            myFuse.transform.rotation = fuseSlotLocation.rotation;
        }
            
    }

    //Called when an object enters the slot
    private void OnTriggerEnter(Collider other)
    {

        GameObject otherGO = other.gameObject;

       if (otherGO.tag == "Fuse")
        {
            myFuse = otherGO.gameObject;
        }


    }

    //Called when an object exits the slot
    private void OnTriggerExit(Collider other)
    {

        GameObject otherGO = other.gameObject;

        if (otherGO.tag == "Fuse")
        {
            other.GetComponent<Rigidbody>().isKinematic = false; 
            other.transform.parent = null;
            myFuse = null;
        }

    }



}
