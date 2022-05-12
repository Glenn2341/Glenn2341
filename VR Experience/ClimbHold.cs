//Glenn Findlay

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//This script moves the player when they interact with the ladder
public class ClimbHold : MonoBehaviour
{

    private bool grabbed = false;
    private bool grabbedRH;

    private CharacterController characterController;

    private GameObject player;
    private OVRPlayerController OVRplayerControllerScript;

    // Start is called before the first frame update
    void Start()
    {
        player = GameObject.FindGameObjectWithTag("OVRPlayerController");
        OVRplayerControllerScript = player.GetComponent<OVRPlayerController>();
        characterController = player.GetComponent<CharacterController>();
      
    }

    //FixedUpdate is called once per physics update (not tied to framerate)
    private void FixedUpdate()
    {


        // determine if the rung is grabbed and by which hand
        if (transform.GetComponent<OVRGrabbable>().isGrabbed)
        {
            grabbed = true;
            OVRGrabber grabber = transform.GetComponent<OVRGrabbable>().grabbedBy;
            if (grabber.tag == "RHand")
            {
                grabbedRH = true;
            }
            else grabbedRH = false;
        }
        else
        {
            grabbed = false;
            grabbedRH = false;

        }


        if (grabbed)
        {          
            movePlayer();
        }

    }

    // move the player when they move their controller
    void movePlayer()
    {

        Vector3 moveVector = new Vector3(0,0,0);
        Vector3 localControllerVelcoity;

        //determine velocity of controller
        if (grabbedRH)
        {
            localControllerVelcoity = OVRInput.GetLocalControllerVelocity(OVRInput.Controller.RHand);

        }
        else
        {
            localControllerVelcoity = OVRInput.GetLocalControllerVelocity(OVRInput.Controller.LHand);       
        }

        //move character contrary to direciton of controller
        moveVector.y = -localControllerVelcoity.y;
        characterController.Move(moveVector * Time.fixedDeltaTime);
      
    }

}
