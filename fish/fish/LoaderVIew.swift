//
//  LoaderVIew.swift
//  fish
//
//  Created by Grisel Angelica Perez Quezada on 12/02/23.
//

import Lottie
import UIKit

public class LoaderView: UIView {
    
    private var lottieAnimation: LottieAnimationView!
   
    private var file: String = ""
    
    override init(frame: CGRect) {
        let nameFile = file
        super.init(frame: frame)
        commonInit(nameFile: nameFile)
    }

    required init?(coder aDecoder: NSCoder){
        super.init(coder: aDecoder)
       
    }
    
    func commonInit(nameFile: String){
        
        // Create Animation object
        if(nameFile != ""){
            lottieAnimation = .init(name: nameFile)
            
            lottieAnimation.frame = CGRect(x: 0, y: 0, width: 400, height: 400)
            lottieAnimation.translatesAutoresizingMaskIntoConstraints = false
            
            lottieAnimation.center = self.center
            lottieAnimation.animationSpeed = 0.6
            lottieAnimation.contentMode = .scaleAspectFill
            
            self.addSubview(lottieAnimation)
            
            lottieAnimation.play()
            
            lottieAnimation.centerXAnchor.constraint(equalTo: centerXAnchor).isActive = true
            lottieAnimation.centerYAnchor.constraint(equalTo: centerYAnchor).isActive = true
            lottieAnimation.widthAnchor.constraint(equalToConstant: 280).isActive = true
            lottieAnimation.heightAnchor.constraint(equalToConstant: 208).isActive = true
        }
       
        
        
        

        
    }

}

