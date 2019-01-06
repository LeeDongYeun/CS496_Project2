module.exports = function(app, User)
{
	//register
	
	app.post('/api/register', function(req, res){
		User.findOne({id: req.body.id}, function(err, user){
			if(err)
				return res.json({result: 0});
			if(user) 
				return res.send("There already exist user");
			var user = new User();
			user.id = req.body.id;
			user.password = req.body.password;
			user.userName = req.body.userName;

			user.save(function(err){
				if(err){
					console.error(err);
					res.json({result: 1});
					return;
				}

				res.json({result : 2});
			});
		});
	});

	//login
	
	app.get('/api/login/:id/:password', function(req, res){
		User.findOne({id: req.params.id}, function(err, user){
			if(err)
				return res.json({result: 0});
			if(!user)
				return res.json({result: 1});
			if(user.password === req.params.password)
				return res.json({result: 2});

			return res.json({result: 3});
		});
	});

	
}