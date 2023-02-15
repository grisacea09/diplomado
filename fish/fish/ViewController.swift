//
//  ViewController.swift
//  fish
//
//  Created by Grisel Angelica Perez Quezada on 12/02/23.
//

import Lottie
import UIKit

class ViewController: UIViewController {

   var animaciones = LoaderView()
   
    let segmentedControl: UISegmentedControl = {
      let control = UISegmentedControl(items: ["Anim1","Anim2","Anim3"])
        control.frame = CGRect(x: 10, y: 70, width:  350, height: 30)
        control.selectedSegmentIndex = 0
        //control.addTarget(self, action: #selector(didChangeValue(segment:)), for: .valueChanged)
        control.addTarget(self, action: #selector(didChangeSegmentedControl), for: .valueChanged)
            
       
          
      return control
    }()
    
    let firstView: UIView = {
      let view = UIView()
        view.backgroundColor = UIColor.green
      view.translatesAutoresizingMaskIntoConstraints = false
      return view
    }()
    
    let secondView: UIView = {
      let view = UIView()
      view.backgroundColor = UIColor.red
      view.translatesAutoresizingMaskIntoConstraints = false
       
      return view
    }()
    
    let threeView: UIView = {
      let view = UIView()
        view.backgroundColor = UIColor.yellow
      view.translatesAutoresizingMaskIntoConstraints = false
        
       
      return view
    }()
   
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
               
        segmentedControl.frame = CGRect(x: 10, y: 60, width:self.view.bounds.width - 20, height: 30)
        self.view.addSubview(segmentedControl)
    
        
        
        
        animaciones.frame = view.bounds
        animaciones.center = self.view.center
        animaciones.commonInit(nameFile: "fish")
        view.addSubview(firstView)
        view.backgroundColor = .cyan
        firstView.addSubview(animaciones)
        
    }
    
   
   
    @objc private func didChangeSegmentedControl(sender: UISegmentedControl) {
            print("UISegmentedControl Value: \(sender.selectedSegmentIndex)")
        
      
        animaciones.removeFromSuperview()
        print("remove de la supervista")
        animaciones = LoaderView()
        animaciones.frame = view.bounds
        animaciones.center = self.view.center
        
        
     
        
        if(sender.selectedSegmentIndex == 0){
            
            view.backgroundColor = .cyan
            animaciones.commonInit(nameFile: "agency")
            firstView.addSubview(animaciones)
          
        
        }
        else if(sender.selectedSegmentIndex == 1){
            
           
            view.backgroundColor = .yellow
          
            animaciones.commonInit(nameFile: "home")
            firstView.addSubview(animaciones)
            
      
            
        }
        else if(sender.selectedSegmentIndex == 2){
            
        
            view.backgroundColor = .systemPink
            animaciones.commonInit(nameFile: "cupid")
            firstView.addSubview(animaciones)
        
           
            
        }
        
        }
   

}

