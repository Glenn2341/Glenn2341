//Glenn Findlay

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//Represents a slottable fuse
public class Fuse : MonoBehaviour
{
    //Serialized Fields are editable in GUI
    [SerializeField] private bool burned;
    [SerializeField] private Material burnedMat;
    [SerializeField] private GameObject prong1;
    [SerializeField] private GameObject prong2;
    [SerializeField] private int fuseType;

    // Start is called before the first frame update
    void Start()
    {
        if (burned)
        {
            prong1.GetComponent<Renderer>().material = burnedMat;
            prong2.GetComponent<Renderer>().material = burnedMat;
        }         
    }

    public bool isBurned()
    {
       return burned;
    }

    public int getfuseType()
    {
        return fuseType;
    }

}
