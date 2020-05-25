import React from "react";
import { Container,Row,Col } from "react-bootstrap";
import AchievmentModal from "../SmartGadgets/AchievementModal";

import Spinner from "react-bootstrap/Spinner";

import { appContext } from "../../appContext"

class ProfileAchievments extends React.Component {
    state = {user : null};
    static contextType = appContext;
    async componentDidMount() {
        this.setState({user:this.context.user});
    }
    
    render() {
        console.log("achievments");
        
        
        if(this.state.user == null){
            return <Container className="p-5">
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        </Container>;
        }else{
            console.log(this.state.user.travel_journal);
            return <Container className="userAchievements">
                <Row>
                    <div className="window radius fullwidth achievements">
                    <h5>Special</h5>
                    <p>Aliquam ante. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Etiam commodo dui eget wisi. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Vestibulum erat nulla, ullamcorper nec, rutrum non, nonummy</p>
                        {this.state.user.travel_journal.special.map(achievement => 
                            <div className="achievement">
                                <div className="circle">
                                    <AchievmentModal icon={achievement.icon}
                                    title={achievement.name}
                                    description={achievement.description} />
                                </div>
                                <span>{achievement.name}</span>
                            </div>
                        )}
                        <h6>Collected {this.state.user.travel_journal.special.length}/15</h6>
                    </div>
                </Row>
                <Row>
                    <div className="window radius fullwidth achievements">
                    <h5>Categorized</h5>
                    <p>Ccillum dolore eu fugiat nulla pariatur. Vestibulum erat nulla, ullamcorper nec, rutrum non, nonumm rutrumrut nulla, ullamcorper nec rum y</p>
                        {this.state.user.travel_journal.categorized.map(achievement => 
                            <div className="achievement">
                                <div className="circle">
                                    <AchievmentModal icon={achievement.icon}
                                    title={achievement.name}
                                    description={achievement.description} />
                                </div>
                                <span>{achievement.name}</span>
                            </div>
                        )}
                        <h6>Collected {this.state.user.travel_journal.categorized.length}/8</h6>
                    </div>
                </Row>
                <Row>
                <div className="window radius fullwidth achievements">
                    <h5>Certifications</h5>
                    <p>Aliquam ante. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sedit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
                        {this.state.user.travel_journal.certificates.map(achievement => 
                            <div className="achievement">
                                <div className="circle">
                                    <AchievmentModal icon={achievement.icon}
                                    title={achievement.name}
                                    description={achievement.description} />
                                </div>
                                <span>{achievement.name}</span>
                            </div>
                        )}
                        <h6>Collected {this.state.user.travel_journal.certificates.length}/5</h6>
                    </div>
                </Row>
            </Container>;
        }
    }
}
export default ProfileAchievments;
