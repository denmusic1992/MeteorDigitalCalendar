import UIKit
import app

class ViewController: UIViewController, RegistrationViewInterface {
    func authorizationResult(success: Bool, reason: String?) {
        
    }
    
    func registerResult(success: Bool, reason: String?) {
        print(reason ?? "все ок!")
    }
    
    func registrationResult(success: Bool, errors: NSMutableArray?, messageError: String?) {
        
    }
    
    func resendEmailResult(success: Bool, message: String?) {
        
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        label.text = Proxy().proxyHello()
        let settings: Settings = AppleSettings(delegate: UserDefaults.standard)
        registrationPresenter = RegistrationPresenterImpl(registrationView: self, settings: settings)
        
        registrationPresenter?.registerDevice()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    @IBOutlet weak var label: UILabel!
    private var registrationPresenter: RegistrationPresenter?
}
